import torch
import torch.nn as nn
import torch.nn.functional as F
        
# --- Transformer Modules ---
class MultiheadAttention(nn.Module): # Multihead Attention
    def __init__(self, embed_dim, num_heads, dropout=0.0):
        super().__init__()
        self.attention = nn.MultiheadAttention(embed_dim, num_heads, dropout)
        self.normalize = nn.LayerNorm(embed_dim)
    # 文本分类器时: input是txt_features, query是topic_embed, 在ClsGen时: input是额外信息的txt_features, query图像特征D_img
    def forward(self, input, query, pad_mask=None, att_mask=None): # input: (B, V, E), query: (B, Q, E), pad_mask: (B, V), att_mask: (B, V, V);#input/query(B,T+L,E)->;pad_mask(B,T+L);att_mask(T+L,T+L);
        # pad_mask是为了masked multihead attention;att_mask是为了self-attention?
        input = input.permute(1,0,2) # (V,B,E); V: sequence length, B: batch size, E: embedding dimension
        query = query.permute(1,0,2) # (Q,B,E); Q: sequence length, B: batch size, E: embedding dimension
        # key_padding_mask用来遮蔽<PAD>以避免pad token的embedding输入。形状要求：（N,S）; attn_mask2维或者3维的矩阵。用来避免指定位置的embedding输入。2维矩阵形状要求：（L, S）；也支持3维矩阵输入，形状要求：（N*num_heads, L, S）
        # attn_mask只用于Decoder训练时的解码过程，作用是掩盖掉当前时刻之后的信息，让模型只能看到当前时刻（包括）之前的信息。
        embed, att = self.attention(query, input, input, key_padding_mask=pad_mask, attn_mask=att_mask) # embed(Q,B,E), (B,Q,V);
        # 文本分类器时: 这里完成公式(2)的计算, D_txt = embed
        embed = self.normalize(embed + query) # (Q,B,E), layer normalization, 文本分类时: 就是公式(4)
        embed = embed.permute(1,0,2) # (B,Q,E)
        return embed, att # (B,Q,E), (B,Q,V), embed是D_fused, att是attention_heatmap
    
class PointwiseFeedForward(nn.Module):
    def __init__(self, emb_dim, fwd_dim, dropout=0.0): # emb_dim=256, fwd_dim=256
        super().__init__()
        self.fwd_layer = nn.Sequential(
            nn.Linear(emb_dim, fwd_dim),
            nn.ReLU(),
            nn.Dropout(dropout),
            nn.Linear(fwd_dim, emb_dim),
        ) # (B,L,E) -> (B,L,E)
        self.normalize = nn.LayerNorm(emb_dim)

    def forward(self, input): #input (B,L,E)
        output = self.fwd_layer(input) # (B,L,E)->(B,L,E)
        output = self.normalize(output + input) # (B,L,E)
        return output

class TransformerLayer(nn.Module): #一个transformer包含3个transformer layer;encoder包含1个,decoder包含2个
    def __init__(self, embed_dim, num_heads, fwd_dim, dropout=0.0): #embed_dim=256, num_heads=8, fwd_dim=256, dropout=0.1
        super().__init__()
        self.attention = MultiheadAttention(embed_dim, num_heads, dropout) # 多头注意力机制
        self.fwd_layer = PointwiseFeedForward(embed_dim, fwd_dim, dropout) # 

    def forward(self, input, pad_mask=None, att_mask=None): #input(B,L,E), pad_mask(B,L), att_mask(B,L,L);#input(B,T+L,E)->;pad_mask(B,T+L);att_mask(T+L,T+L);
        emb, att = self.attention(input,input,pad_mask,att_mask) #第一个input是K/V;第二个input是Q;emb(B,L,E), att(B,L,L);emb(B,T+L,E), att(B,T+L,T+L);
        emb = self.fwd_layer(emb) # emb(B,T+L,E)
        return emb, att

class TNN(nn.Module): # 对文本进行embedding和位置编码;然后进行多层transformer进行encoder编码
    def __init__(self, embed_dim, num_heads, fwd_dim, dropout=0.1, num_layers=1,
                num_tokens=1, num_posits=1, token_embedding=None, posit_embedding=None):
        super().__init__()
        self.token_embedding = nn.Embedding(num_tokens, embed_dim) if not token_embedding else token_embedding # (V,E);ids->256; 1000->256
        self.posit_embedding = nn.Embedding(num_posits, embed_dim) if not posit_embedding else posit_embedding # (L,E);ids->256 
        self.transform = nn.ModuleList([TransformerLayer(embed_dim, num_heads, fwd_dim, dropout) for _ in range(num_layers)])
        self.dropout = nn.Dropout(dropout)
        
    def forward(self, token_index=None, token_embed=None, pad_mask=None, pad_id=-1, att_mask=None): #token_index(B,L);pad_mask(B,L)
        if token_index != None: #token_index(B,L);
            if pad_mask == None: #pad_mask(B,L)
                pad_mask = (token_index == pad_id) # (B,L)
            posit_index = torch.arange(token_index.shape[1]).unsqueeze(0).repeat(token_index.shape[0],1).to(token_index.device) # (B,L)
            posit_embed = self.posit_embedding(posit_index) # (B,L,E), 对应位置的编码
            token_embed = self.token_embedding(token_index) # (B,L,E), 对应词的编码
            final_embed = self.dropout(token_embed + posit_embed) # (B,L,E);位置编码,词编码相加
        elif token_embed != None:
            posit_index = torch.arange(token_embed.shape[1]).unsqueeze(0).repeat(token_embed.shape[0],1).to(token_embed.device) # (B,L)
            posit_embed = self.posit_embedding(posit_index) # (B,L,E)
            final_embed = self.dropout(token_embed + posit_embed) # (B,L,E)
        else:
            raise ValueError('token_index or token_embed must not be None')

        for i in range(len(self.transform)): #多层transformer
            final_embed = self.transform[i](final_embed, pad_mask, att_mask)[0] # (batch, length, embed_dim)
            
        return final_embed # (B,L,E)

# --- Convolution Modules ---
class CNN(nn.Module): #提取视觉特征;输入BV个图像,输出BV个特征图和BV个特征
    def __init__(self, model, model_type='resnet'):
        super().__init__()
        if 'res' in model_type.lower(): # resnet, resnet-50, resnest-50, ...
            modules = list(model.children())[:-1] # Drop the FC layer
            self.feature = nn.Sequential(*modules[:-1])
            self.average = modules[-1]
        elif 'dense' in model_type.lower(): # densenet, densenet-121, densenet121, ...
            modules = list(model.features.children())[:-1]
            self.feature = nn.Sequential(*modules)
            self.average = nn.AdaptiveAvgPool2d((1, 1))
        else:
            raise ValueError('Unsupported model_type!')
        
    def forward(self, input): #input(V*B,3,256,256)
        wxh_features = self.feature(input) # input(2*B,3,256,256);wxh_features(2*B,1024,8,8)
        avg_features = self.average(wxh_features) #wxh_features(2*B,1024,8,8);avg_features(2*B,1024,1,1)
        avg_features = avg_features.view(avg_features.shape[0], -1) #avg_features(2*B,1024,1,1)->(2*B,1024)
        return avg_features, wxh_features #wxh_features(2*B,1024,8,8);avg_features(2*B,1024)

class MVCNN(nn.Module): # Multi-View CNN; 输入图像的多个视角,提取视觉特征;输入BV个图像,输出B个特征图和B个特征;是对CNN的封装;使用类似max pool的方法消除了V这个维度
    def __init__(self, model):
        super().__init__()
        self.model = model

    def forward(self, input): #input[(B,V,C,H,W),(B,V)]
        img = input[0] # (B,V,C,W,H) / (B,2,V,C,W,H)
        pos = input[1] # (B,V)
        
        if img.dim() == 6: # (B,2,V,C,W,H) , 如果使用了对比学习
            # 重复pos, (B,V)->(2*B,V)
            pos = pos.repeat(2,1) # (2*B,V)
            # 交换2和B的位置
            img = img.permute(1, 0, 2, 3, 4, 5) # (B, 2, V, C, W, H)
            img = img.reshape(img.shape[0]*img.shape[1], img.shape[2], img.shape[3], img.shape[4], img.shape[5]) # (2*B,V,C,W,H)
        B,V,C,W,H = img.shape

        # if img.dim() == 5: # 如果没有使用对比学习
        img = img.view(B*V,C,W,H)
        # elif img.dim() == 6: # 如果使用了对比学习
        #     img = img.view(B*V*2,C,W,H)
        avg, wxh = self.model(img) #wxh(BV,1024,8,8);avg(BV,1024)
        avg = avg.view(B,V,-1) # avg(B,V,F)
        wxh = wxh.view(B,V,wxh.shape[-3],wxh.shape[-2],wxh.shape[-1]) # wxh(B,V,F,W,H)
        
        msk = (pos == -1) # msk(B,V);如果是填充的图像，就是True
        msk_wxh = msk.view(B,V,1,1,1).float() # msk_wxh(B,V,1,1,1)
        msk_avg = msk.view(B,V,1).float() # msk_avg(B,V,1)
        wxh = msk_wxh * (-1) + (1-msk_wxh) * wxh # wxh(B,V,F,W,H);前面的一项是真实图像是0,填充图像是-1;后面一项(1-msk_wxh)是真实图像是1,填充图像是0;这样真实图像的特征图就过滤出来了,填充图像的特征图就是-1
        avg = msk_avg * (-1) + (1-msk_avg) * avg #同理,最终结果是过滤出真实图像的特征图,填充图像得到了全为-1

        wxh_features = wxh.max(dim=1)[0] # wxh(B,V,F,W,H)->wxh_features(B,F,W,H);这样可以把填充图像信息过滤掉;如果V个图像都是真实图像,会类似于MaxPooling的效果
        avg_features = avg.max(dim=1)[0] # avg_features(B,F);这样可以把填充图像信息过滤掉;如果V个图像都是真实图像,会类似于MaxPooling的效果
        return avg_features, wxh_features #avg_features(B,F);wxh_features(B,F,W,H);

# --- Main Moduldes ---
class Classifier(nn.Module): #分类器,输入图像/文本,输出注意力和分类特征
    def __init__(self, num_topics, num_states, cnn=None, tnn=None,
                fc_features=2048, embed_dim=128, num_heads=1, dropout=0.1): #num_topics=114;num_states=2;fc_features=2048;embed_dim=256;num_heads=8;dropout=0.1
        super().__init__()
        
        # For img & txt embedding and feature extraction
        self.cnn = cnn # None; 在单独的文本分类器时候cnn为None,在多模态分类器时候cnn为CNN模型
        self.tnn = tnn # TransformerEncoder; encoder操作; transformer encoder network
        self.img_features = nn.Linear(fc_features, num_topics * embed_dim) if cnn != None else None # None;使用线性层把图像特征转换为类别特征
        self.txt_features = MultiheadAttention(embed_dim, num_heads, dropout) if tnn != None else None # 使用多头注意力把文本特征和topic特征结合起来
        
        # For classification
        self.topic_embedding = nn.Embedding(num_topics, embed_dim) # 114->256, 对每个类别进行编码
        self.state_embedding = nn.Embedding(num_states, embed_dim) # 2->256, 对每个状态进行编码
        self.attention = MultiheadAttention(embed_dim, num_heads)
        
        # Some constants
        self.num_topics = num_topics # 114
        self.num_states = num_states # 2
        self.dropout = nn.Dropout(dropout) # 0.1
        self.normalize = nn.LayerNorm(embed_dim) # 256

    def forward(self, img=None, txt=None, lbl=None, txt_embed=None, pad_mask=None, pad_id=3, threshold=0.5, get_embed=False, get_txt_att=False):
        # img[(B,V,3,256,256),(B,V)];txt(B,L);lbl是分类label标签(B,T);threshold=0.15;get_embed=True;get_txt_att=False
        # --- Get img and txt features ---
        if img != None: # (B,C,W,H) or ((B,V,C,W,H), (B,V)); ClsGen任务; ClsGenInt任务;
            img_features, wxh_features = self.cnn(img) #输入图像的多视图,输出图像的特征;img_features(B,F);wxh_features(B,F,W,H);
            img_features = self.dropout(img_features) # img_features(B,F);D_img
            # img_features = img_features[:txt.shape[0]]
            # img_features(batch,1024), img_features是图中的x
        if txt != None: #对文本进行embedding和位置编码;然后进行多层transformer进行encoder编码; Cls任务; ClsGen任务; ClsGenInt任务;
            if pad_id >= 0 and pad_mask == None:
                pad_mask = (txt == pad_id) # txt(B,L) -> pad_mask(B,L);如果是填充的文本，就是True
            # transform encoder 操作, 获得文本特征向量
            txt_features = self.tnn(token_index=txt, pad_mask=pad_mask) #D_txt;txt_features(B,L,E);对文本进行embedding和位置编码;然后进行多层transformer进行encoder编码
            # txt_features(batch, length, embed_dim), txt_features是图中的h
        elif txt_embed != None: # ClsGenInt任务:训练Int阶段
            txt_features = self.tnn(token_embed=txt_embed, pad_mask=pad_mask) # (B,L,E)

        # --- Fuse img and txt features ---
        if img != None and (txt != None or txt_embed != None): # 具有图像和文本; ClsGen任务; ClsGenInt任务;
            topic_index = torch.arange(self.num_topics).unsqueeze(0).repeat(txt_features.shape[0],1).to(txt_features.device) # (B,T)
            state_index = torch.arange(self.num_states).unsqueeze(0).repeat(txt_features.shape[0],1).to(txt_features.device) # (B,C)
            topic_embed = self.topic_embedding(topic_index) # topic_embed(B,T,E), 这个用在公式(2)中的softmax()的Q的生成
            state_embed = self.state_embedding(state_index) # state_embed(B,C,E), 这个公式(5)中的S的生成
            # 公式(3) img_features(batch,1024)->(batch,114,256), 获得每个类别的特征向量
            img_features = self.img_features(img_features).view(img_features.shape[0], self.num_topics, -1) # (B,F) --> (B,T*E) --> (B,T,E), 每一个[1]维度就是一个类别的特征向量, 要的就是这个, 用来计算对比损失
            # 公式(2) 输出的txt_features就是D_txt, 输入的txt_features就是H, topic_embed就是Q, 输出txt_attention就是softmax()后的attention_heatmap
            txt_features, txt_attention = self.txt_features(txt_features, topic_embed, pad_mask) # txt_features就是论文中的D_topic(B,L,E)->(B,T,E),topic_embed作为Q查询(B,T,E);pad_mask(B,L);txt_attention(B,T,L)
            final_embed = self.normalize(img_features[:txt_features.shape[0]] + txt_features) #(B,T,E);疾病相关主题的语境化嵌入(contextualized embedding in terms of disease-related topics)
            # 公式(4) final_embed就是D_fused
        elif img != None: # 只具有图像信息
            topic_index = torch.arange(self.num_topics).unsqueeze(0).repeat(img_features.shape[0],1).to(img_features.device) # (B,T)
            state_index = torch.arange(self.num_states).unsqueeze(0).repeat(img_features.shape[0],1).to(img_features.device) # (B,C)
            topic_embed = self.topic_embedding(topic_index) # (B,T,E)
            state_embed = self.state_embedding(state_index) # (B,C,E)

            img_features = self.img_features(img_features).view(img_features.shape[0], self.num_topics, -1) # (B,F) --> (B,T*E) --> (B,T,E)   
            final_embed = img_features # (B,T,E)
            
        elif txt != None or txt_embed != None: # 只具有文本信息, 是文本分类任务
            topic_index = torch.arange(self.num_topics).unsqueeze(0).repeat(txt_features.shape[0],1).to(txt_features.device) # (B,T);(B,114)
            state_index = torch.arange(self.num_states).unsqueeze(0).repeat(txt_features.shape[0],1).to(txt_features.device) # (B,C);(B,2)
            topic_embed = self.topic_embedding(topic_index) # (B,T,E), 对标签序号进行embedding
            state_embed = self.state_embedding(state_index) # (B,C,E);state_embed就是论文中的D_state(B,C,E)
            # txt_features(batch, class_num, embed_dim); txt_attention(batch, class_num, length)
            txt_features, txt_attention = self.txt_features(txt_features, topic_embed, pad_mask) #产生的txt_features就是论文中的D_topic表示K/V(B,L,E)->(B,T,E); topic_embed表示Q(B,T,E), pad_mask(B,L);txt_attention(B,T,L)
            final_embed = txt_features # (B,T,E), 文本分类时: txt_features就是D_fused, txt_attention就是attention_heatmap
            
        else: # 无图像信息，无文本信息
            raise ValueError('img and (txt or txt_embed) must not be all none')
        
        # Classifier output, 
        # 公式(5): 文本分类任务: final_embed是D_fused, state_embed是S, 也就是state_embedding, 输出:att就是公式(5)中的p, 表示疾病i属于第j种情况的logits
        emb, att = self.attention(state_embed, final_embed) #把文本和图像的融合特征和分类特征进行融合; emb(B,T,E), att是注意力,同时也是分类的概率(B,T,2);state_embed作为K和V(B,2,E), final_embed是融合特征作为Q(B,T,E);
        # emb(batch, class_num, embed_dim)用于报告生成; att(batch, class_num, 2)用于分类
        if lbl != None: # Teacher forcing ;训练阶段使用真实标签y/lbl;其他阶段使用预测概率att的标签
            # 公式(7) ClsGen任务:训练/测试,emb是D_states
            # 公式(7) ClsGenInt任务:训练/测试,emb是D_states
            emb = self.state_embedding(lbl) #emb就是论文中的D_states; 对分类label标签进行embedding; lbl(B,T);emb(B,T,E)
        else: 
            # 非训练阶段，使用预测概率att的标签; 
            # 文本分类任务: 训练阶段, att就是公式(7)中的p, 表示疾病i属于第j种情况的logits, att[:,:,1]选取[1]是因为表示是该疾病的概率, emb表示公式(7)中的D_states
            # 公式(7) ClsGen任务:推理,
            # 公式(7) ClsGenInt任务:推理/Int阶段, 不过Int阶段不需要emb
            emb = self.state_embedding((att[:,:,1] > threshold).long()) # (B,T,1)->(B,T,E) ;emb(batch, class_num, embed_dim); att(batch, class_num, 2)
            # att[:,:,0] 表示注意力权重中当前时间步的位置，而 att[:,:,1] 表示对应位置的注意力权重值, 所以使用的[1]
        if get_embed: #(final_embed + emb)是论文中丰富的疾病嵌入(enriched disease embedding);编码着相关主题的疾病和症状; ClsGenInt任务:训练
            return att, final_embed + emb, img_features #文本和图像的融合特征和分类特征的注意力att(B,T,C), 标签特征和文本图像特征融合; (B,T,E);
        elif get_txt_att and (txt != None or txt_embed != None):
            return att, txt_attention # (B,T,C), (B,T,L)
        else:
            return att # (B,T,C);att(B,T,2) #文本分类任务训练阶段: att(batch, class_num, 2)

class Generator(nn.Module):
    def __init__(self, num_tokens, num_posits, embed_dim=128, num_heads=1, fwd_dim=256, dropout=0.1, num_layers=12):
        super().__init__()
        self.token_embedding = nn.Embedding(num_tokens, embed_dim) # E表示词向量维度;把词id转换为词向量
        self.posit_embedding = nn.Embedding(num_posits, embed_dim) # E表示词向量维度;把位置id转换为位置向量
        self.transform = nn.ModuleList([TransformerLayer(embed_dim, num_heads, fwd_dim, dropout) for _ in range(num_layers)]) # TransformerLayer表示一个Transformer层,在TNN中也使用了
        self.attention = MultiheadAttention(embed_dim, num_heads) # MultiheadAttention表示多头注意力机制
        self.num_tokens = num_tokens # 最大句子长度;1000
        self.num_posits = num_posits # 最大位置序列长度;1000
        
    def forward(self, source_embed, token_index=None, source_pad_mask=None, target_pad_mask=None, max_len=300, top_k=1, bos_id=1, pad_id=3, mode='eye'):
        # source_embed就是encoder输出,即隐态h,标签特征和文本图像特征融合(B,T,E);token_index目标(B,L);source_pad_mask(B,L);
        if token_index != None: # --- Training/Testing Phase 推理时候token_index内容全为1,表输出,在推理时L是变长的,表示当前已知序列长度 ---
            # Adding token embedding and posititional embedding.
            posit_index = torch.arange(token_index.shape[1]).unsqueeze(0).repeat(token_index.shape[0],1).to(token_index.device) # (1,L) --> (B,L)
            posit_embed = self.posit_embedding(posit_index) # (B,L,E)
            token_embed = self.token_embedding(token_index) # (B,L,E)
            target_embed = token_embed + posit_embed # 文本加入位置编码,然后进行自注意力计算(B,L,E)
            
            # Make embedding, attention mask, pad mask for Transformer Decoder
            final_embed = torch.cat([source_embed,target_embed], dim=1) #cat是为了使用encoder输出的特征,final_embed(B,T+L,E)
            if source_pad_mask == None: #source_pad_mask(batch,114)内容值全为False
                source_pad_mask = torch.zeros((source_embed.shape[0],source_embed.shape[1]),device=source_embed.device).bool() # source_pad_mask(B,T)
            if target_pad_mask == None:
                target_pad_mask = torch.zeros((target_embed.shape[0],target_embed.shape[1]),device=target_embed.device).bool() # (B,L)
            pad_mask = torch.cat([source_pad_mask,target_pad_mask], dim=1) #进行concat也是为了可以并行,一次性产生所有的输出(B,T+L)
            att_mask = self.generate_square_subsequent_mask_with_source(source_embed.shape[1], target_embed.shape[1], mode).to(final_embed.device) # att_mask(T+L,T+L);产生左上角方阵对角线为0其余为-inf;右下角方阵下三角为0其余为-inf;左下角矩阵为0;右上角矩阵为-inf;这样能够达到的目的是:先产生label,然后使用label信息来产生报告
            # att_mask对角线及对角线以下全为0,其余全为-inf, 把左上角114*114区域变成对角线为0,其余为-inf的方阵; 根据你的描述，这里的att_mask是一个上三角矩阵，将左上角114*114区域变成对角线为0，其余为负无穷大的方阵。这意味着在计算Attention时，每个位置只能考虑它之前的位置和对角线上的位置，而不能考虑它之后的位置。这种做法通常用于避免模型看到未来的信息，或者控制生成任务的输出顺序。
            # Transformer Decoder
            for i in range(len(self.transform)):
                final_embed = self.transform[i](final_embed,pad_mask,att_mask)[0] #decoder第一个多头注意力;final_embed(B,T+L,E)->;pad_mask(B,T+L);att_mask(T+L,T+L);
                # final_embed(batch,114+1000,256)
            # Make prediction for next tokens
            token_index = torch.arange(self.num_tokens).unsqueeze(0).repeat(token_index.shape[0],1).to(token_index.device) # token_index(B,1000);(1,K) --> (B,K)
            token_embed = self.token_embedding(token_index) # train时(B,L,E);infer时(B,1000,E), 为什么只有token_embed没有posit_embed? 因为token_index全为1,所以不需要位置编码
            emb, att = self.attention(token_embed,final_embed) #decoder第二个多头注意力; token_embed(B,L,E);emb(B,T+L,E);final_embed(B,T+L,E);att(B,T+L,L)
            # token_embed(batch,1000,256)表示Q;final_embed(batch,114+1000,256)表示K,V;att(batch,114+1000,1000);emb(batch,114+1000,256)
            # Truncate results from source_embed ; 剔除掉前面的分类信息,只保留后边的文本预测信息
            emb = emb[:,source_embed.shape[1]:,:] # (B,L,E);emb(B,T+L,E)->(B,L,E);source_embed(B,T,E);
            att = att[:,source_embed.shape[1]:,:] # (B,L,K);att(B,T+L,vocab_size)->(B,L,vocab_size);source_embed(B,T,E);
            return att, emb #att(B,L,L)等同于预测概率,可以用于计算交叉熵损失;emb(B,L,E)
        
        else: # --- Inference Phase ---source_embed(batch,114,256)是D_enriched,source_pad_mask=None,max_len=300,top_k=1,bos_id=1,pad_id=3
            return self.infer(source_embed, source_pad_mask, max_len, top_k, bos_id, pad_id)

    def infer(self, source_embed, source_pad_mask=None, max_len=100, top_k=1, bos_id=1, pad_id=3): #
        # source_embed就是encoder输出,即隐态h(B,T,E);max_len=300;top_k=1;bos_id=1;pad_id=3;
        outputs = torch.ones((top_k, source_embed.shape[0], 1), dtype=torch.long).to(source_embed.device) * bos_id # outputs(K,B,1) 全是<s>也就是全是<1>表示开始
        scores = torch.zeros((top_k, source_embed.shape[0]), dtype=torch.float32).to(source_embed.device) # scores(K,B)
        # outputs(1,batch,1)内容值全为1,表示<bos>;scores(1,batch)内容值全为0,表示初始分数为0
        for _ in range(1,max_len): # 从1开始,因为第一个位置已经是<s>了,不需要再预测了,直接从第二个位置开始预测,预测到max_len结束,也就是预测max_len-1个位置
            possible_outputs = []
            possible_scores = []

            for k in range(top_k): #top_k=1,采用贪心策略,每次只取概率最大的那个token;top_k>1,采用beam search策略,每次取概率最大的top_k个token
                output = outputs[k] # output(B,L);L变长
                score = scores[k] # score(B)
                # source_embed(batch,114,256)是D_enriched在每次每一个token时都是一样的, output是先前生成的文本, source_pad_mask是输入的mask为改变, target_pad_mask是已有输出和pad的mask
                att, emb = self.forward(source_embed, output, source_pad_mask=source_pad_mask, target_pad_mask=(output == pad_id)) #att(B,L,1000);
                val, idx = torch.topk(att[:,-1,:], top_k) # val(B,K); att(B,L,1000);
                log_val = -torch.log(val) # log_val(B,K) 
                
                for i in range(top_k):
                    new_output = torch.cat([output, idx[:,i].view(-1,1)], dim=-1) # new_output(B,L+1)
                    new_score = score + log_val[:,i].view(-1) # (B)
                    possible_outputs.append(new_output.unsqueeze(0)) # (1,B,L+1)
                    possible_scores.append(new_score.unsqueeze(0)) # (1,B)
            
            possible_outputs = torch.cat(possible_outputs, dim=0) # (K^2,B,L+1)
            possible_scores = torch.cat(possible_scores, dim=0) # (K^2,B)

            # Pruning the solutions
            val, idx = torch.topk(possible_scores, top_k, dim=0) # (K,B)
            col_idx = torch.arange(idx.shape[1], device=idx.device).unsqueeze(0).repeat(idx.shape[0],1) # (K,B)
            outputs = possible_outputs[idx,col_idx] # (K,B,L+1)
            scores = possible_scores[idx,col_idx] # (K,B)

        val, idx = torch.topk(scores, 1, dim=0) # (1,B)
        col_idx = torch.arange(idx.shape[1], device=idx.device).unsqueeze(0).repeat(idx.shape[0],1) # (K,B)
        output = outputs[idx,col_idx] # (1,B,L)
        score = scores[idx,col_idx] # (1,B)
        return output.squeeze(0) # (B,L)
    # #对角线及对角线以下全为0,其余全为-inf, 把左上角114*114区域变成对角线为0,其余为-inf的方阵
    def generate_square_subsequent_mask_with_source(self, src_sz, tgt_sz, mode='eye'): #src_sz=T; tgt_sz=L;产生左上角方阵对角线为0其余为-inf;右下角方阵下三角为0其余为-inf;左下角矩阵为0;右上角矩阵为-inf
        # mask(114+1000), 对角线及对角线以下全为0,其余全为-inf
        mask = self.generate_square_subsequent_mask(src_sz + tgt_sz)#产生下三角全为0,对角线上方全为-inf的mask(T+L,T+L);产生-inf是为了在masked multi-head attention中,对于query和key的计算,不会对padding的部分进行计算,-inf进行softmax后为0,不会对padding的部分进行计算
        if mode == 'one': # model can look at surrounding positions of the current index ith
            mask[:src_sz, :src_sz] = self.generate_square_mask(src_sz)
        elif mode == 'eye': # model can only look at the current index ith
            # 把左上角114*114区域变成对角线为0,其余为-inf的方阵,其余部分不变
            mask[:src_sz, :src_sz] = self.generate_square_identity_mask(src_sz) #产生对角线全为0,其余全为-inf的mask(T,T);前T行T列对角线为0,其余全为-inf;T*T矩阵外不变
        else: # model can look at surrounding positions of the current index ith with some patterns
            raise ValueError('Mode must be "one" or "eye".')
        mask[src_sz:, src_sz:] = self.generate_square_subsequent_mask(tgt_sz)#产生下三角全为0,对角线上方全为-inf的mask(T+L,T+L)
        return mask #对角线及对角线以下全为0,其余全为-inf, 把左上角114*114区域变成对角线为0,其余为-inf的方阵

    def generate_square_subsequent_mask(self, sz): #sz=T+L;产生下三角全为0,对角线上方全为-inf的mask(T+L,T+L)
        mask = (torch.triu(torch.ones(sz, sz)) == 1).transpose(0, 1) # 下三角矩阵(T+L,T+L)
        mask = mask.float().masked_fill(mask == 0, float('-inf')).masked_fill(mask == 1, float(0.0)) #下三角全为0;对角线上方全为-inf;mask(T+L,T+L)
        return mask

    def generate_square_identity_mask(self, sz): #sz=T;产生对角线全为0,其余全为-inf的mask(T,T)
        mask = (torch.eye(sz) == 1)
        mask = mask.float().masked_fill(mask == 0, float('-inf')).masked_fill(mask == 1, float(0.0))
        return mask 

    def generate_square_mask(self, sz):
        mask = (torch.ones(sz,sz) == 1)
        mask = mask.float().masked_fill(mask == 0, float('-inf')).masked_fill(mask == 1, float(0.0))
        return mask

# --- Full Models ---
class ClsGen(nn.Module):
    def __init__(self, classifier, generator, num_topics, embed_dim,bos_id=1, eos_id=2, pad_id=3):
        super().__init__()
        self.classifier = classifier
        self.generator = generator
        self.label_embedding = nn.Embedding(num_topics, embed_dim) #num_topics主题长度114，embed_dim词向量长度256
        self.bos_id = bos_id
        self.eos_id = eos_id
        self.pad_id = pad_id

    def forward(self, image, history=None, caption=None, label=None, threshold=0.15,  max_len=300, get_emb=True):
        # image[(B,V,3,256,256),(B,V)];history(B,L);caption目标(B,L);label(B,T)
        label = label.long() if label != None else label #label变成long类型
        #img_mlc文本图像的融合特征和分类特征的注意力(B,T,C); img_emb标签特征和历史文本图像特征融合(B,T,E),img_emb是论文中丰富的疾病嵌入(enriched disease embedding)
        # ClsGen任务, train: img_mlc(batch,114,2)用于图像分类, img_emb(batch,114,256)用于报告生成
        img_mlc, img_emb, img_features = self.classifier(img=image, txt=history, lbl=label, threshold=threshold, pad_id=self.pad_id, get_embed=True) #输入图像和文本,输出注意力和分类特征; 注意力img_mlc(B,T,C), 分类特征img_emb(B,T,E)
        # 使用img_features计算对比损失
        lbl_idx = torch.arange(img_emb.shape[1]).unsqueeze(0).repeat(img_emb.shape[0],1).to(img_emb.device) # (B,T)
        lbl_emb = self.label_embedding(lbl_idx) # 标签embedding(B,T,E)
        # 公式(8): lbl_emb是D_topic
        if caption != None: #训练阶段
            # 公式(8): img_emb是D_states+D_fused, lbl_emb是D_topic, src_emb是D_enriched
            src_emb = img_emb + lbl_emb #img_emb图像特征(B,T,E);lbl_emb标签特征(B,T,E);src_emb标签特征和文本图像特征融合(B,T,E)
            pad_mask = (caption == self.pad_id) #判断是否是填充,填充为True，非填充为False;pad_mask(B,L)
            # src_emb文本和图像的融合特征(batch,114,256); caption是target(batch,1000); pad_mask是填充的mask(batch,1000)根据caption得到
            cap_gen, cap_emb = self.generator(source_embed=src_emb, token_index=caption, target_pad_mask=pad_mask) #src_emb融合特征(B,T,E);caption目标(B,L);pad_mask(B,L);cap_gen(B,L,L);cap_emb是加权后的文本特征(B,L,E)
            # cap_gen=att(batch,1000,1000)等同于预测概率,可以用于计算交叉熵损失,也等同于生成的文本;cap_emb=emb报告的嵌入向量(batch,1000,256)
            if get_emb: # ClsGenInt任务:训练, ClsGen任务:训练阶段
                return cap_gen, img_mlc, cap_emb, img_features #cap_gen是产生的医疗报告(B,L,vocab_size);img_mlc(B,T,C);cap_emb是论文中的加权嵌入表示,报告的嵌入向量可用于疾病分类(weighted embedding representation)(B,L,E)
            else:
                return cap_gen, img_mlc #cap_gen是产生的医疗报告(medical reports)(B,L,vocab_size);img_mlc(B,T,C)
        else: #推理阶段
            # 公式(8): img_emb是D_states+D_fused, lbl_emb是D_topic, src_emb是D_enriched
            src_emb = img_emb + lbl_emb #img_emb图像和历史文本特征融合, lbl_emb标签特征;src_emb(B,T,E)
            cap_gen = self.generator(source_embed=src_emb, token_index=caption, max_len=max_len, bos_id=self.bos_id, pad_id=self.pad_id) # (B,L,S)
            return cap_gen, img_mlc #cap_gen(B,L);img_mlc(B,T,C)

class ClsGenInt(nn.Module):
    def __init__(self, clsgen, interpreter, freeze_evaluator=True,head_tag=False):
        super().__init__()
        self.clsgen = clsgen
        self.interpreter = interpreter
        self.head_tag = head_tag
        # Freeze evaluator's paramters
        if freeze_evaluator:
            for param in self.interpreter.parameters():
                param.requires_grad = False
        if head_tag:
            self.head = nn.Linear(256, 128)

    def forward(self, image, history=None, caption=None, label=None, threshold=0.15, bos_id=1, eos_id=2, pad_id=3, max_len=300):        
        if caption != None: #训练
            pad_mask = (caption == pad_id)
            cap_gen, img_mlc, cap_emb, img_features = self.clsgen(image, history, caption, label, threshold, bos_id, eos_id, pad_id, max_len, True) #cap_gen(B,L,L);img_mlc(B,T,C);cap_emb是注意力加权后的文本特征(B,L,E); img_features是对比学习需要使用的
            cap_mlc = self.interpreter(txt_embed=cap_emb, pad_mask=pad_mask) #cap_emb是论文中的加权嵌入表示(weighted embedding representation)(B,L,E)
            if self.head_tag:
                img_features = self.head(img_features)
            return cap_gen, img_mlc, cap_mlc, cap_emb, F.normalize(img_features,dim=-1) #cap_gen(batch,length,vocab_size);img_mlc(B,114,2);cap_mlc(B,114,2); cap_emb(batch,length,256)
        else: # 预测/推理;caption为None,表示预测,也称为target
            return self.clsgen(image, history, caption, label, threshold, bos_id, eos_id, pad_id, max_len, False) #cap_gen(B,L,L);img_mlc(B,T,C);