/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is part of dcm4che, an implementation of DICOM(TM) in
 * Java(TM), hosted at https://github.com/dcm4che.
 *
 * The Initial Developer of the Original Code is
 * Agfa Healthcare.
 * Portions created by the Initial Developer are Copyright (C) 2012
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * See @authors listed below
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

package com.bjpowernode.crm.web.hl7rcv;

import ca.uhn.hl7v2.llp.MllpConstants;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.datatype.ST;
import ca.uhn.hl7v2.model.v26.message.ACK;
import ca.uhn.hl7v2.model.v26.message.ORM_O01;
import ca.uhn.hl7v2.parser.EncodingCharacters;
import ca.uhn.hl7v2.parser.PipeParser;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.PatientDao;
import com.bjpowernode.crm.workbench.dao.StudyInfoDao;
import com.bjpowernode.crm.workbench.domain.Patient;
import com.bjpowernode.crm.workbench.domain.StudyInfo;
import com.bjpowernode.crm.workbench.service.RegisterService;
import com.bjpowernode.crm.workbench.service.impl.RegisterServiceImpl;
import org.apache.commons.cli.*;
import org.dcm4che3.hl7.*;
import org.dcm4che3.io.SAXTransformer;
import org.dcm4che3.net.Connection;
import org.dcm4che3.net.Connection.Protocol;
import org.dcm4che3.net.Device;
import org.dcm4che3.net.hl7.HL7Application;
import org.dcm4che3.net.hl7.HL7DeviceExtension;
import org.dcm4che3.net.hl7.HL7MessageListener;
import org.dcm4che3.net.hl7.UnparsedHL7Message;
import org.dcm4che3.tool.common.CLIUtils;
import org.dcm4che3.util.StringUtils;
import org.dcm4che3.util.UIDUtils;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;


/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 */
public class HL7Rcv {
    private StudyInfoDao studyinfoDao = SqlSessionUtil.getSqlSession().getMapper(StudyInfoDao.class);
    private PatientDao patientDao = SqlSessionUtil.getSqlSession().getMapper(PatientDao.class);

    private static final ResourceBundle rb =
            ResourceBundle.getBundle("org.dcm4che3.tool.hl7rcv.messages");
    private static SAXTransformerFactory factory =
            (SAXTransformerFactory) TransformerFactory.newInstance();

    private final Device device = new Device("hl7rcv");
    private final HL7DeviceExtension hl7Ext = new HL7DeviceExtension();
    private final HL7Application hl7App = new HL7Application("*");
    private final Connection conn = new Connection();
    private String storageDir;
    private String charset;
    private Templates tpls;
    private String[] xsltParams;
    private final HL7MessageListener handler = new HL7MessageListener() {

        @Override
        public UnparsedHL7Message onMessage(HL7Application hl7App, Connection conn, Socket s, UnparsedHL7Message msg)
                throws HL7Exception {
            try {

                return HL7Rcv.this.onMessage(msg);//接收到消息首先触发这里，然后把数据交付给本类的onMessage方法
            } catch (Exception e) {
                throw new HL7Exception(
                        new ERRSegment(msg.msh()).setUserMessage(e.getMessage()),
                        e);
            }
        }
    };

    public HL7Rcv() throws IOException {
        conn.setProtocol(Protocol.HL7);
        device.addDeviceExtension(hl7Ext);
        device.addConnection(conn);
        hl7Ext.addHL7Application(hl7App);
        hl7App.setAcceptedMessageTypes("*");
        hl7App.addConnection(conn);
        hl7App.setHL7MessageListener(handler);
    }

    public void setStorageDirectory(String storageDir) {
        this.storageDir = storageDir;
    }

    public void setXSLT(URL xslt) throws Exception {
        tpls = SAXTransformer.newTemplates(
                new StreamSource(xslt.openStream(), xslt.toExternalForm()));
    }

    public void setXSLTParameters(String[] xsltParams) {
        this.xsltParams = xsltParams;
    }

    public void setCharacterSet(String charset) {
        this.charset = charset;
    }

    private static CommandLine parseComandLine(String[] args)
            throws ParseException {
        Options opts = new Options();
        addOptions(opts);
        CLIUtils.addSocketOptions(opts);
        CLIUtils.addTLSOptions(opts);
        CLIUtils.addCommonOptions(opts);
        return CLIUtils.parseComandLine(args, opts, rb, HL7Rcv.class);
    }

    @SuppressWarnings("static-access")
    public static void addOptions(Options opts) {
        opts.addOption(null, "ignore", false, rb.getString("ignore"));
        opts.addOption(Option.builder()
                .hasArg()
                .argName("path")
                .desc(rb.getString("directory"))
                .longOpt("directory")
                .build());
        opts.addOption(Option.builder("x")
                .longOpt("xsl")
                .hasArg()
                .argName("xsl-file")
                .desc(rb.getString("xsl"))
                .build());
        opts.addOption(Option.builder()
                .longOpt("xsl-param")
                .hasArgs()
                .valueSeparator('=')
                .argName("name=value")
                .desc(rb.getString("xsl-param"))
                .build());
        opts.addOption(Option.builder()
                .longOpt("charset")
                .hasArg()
                .argName("name")
                .desc(rb.getString("charset"))
                .build());
        opts.addOption(Option.builder("b")
                .hasArg()
                .argName("[ip:]port")
                .desc(rb.getString("bind-server"))
                .longOpt("bind")
                .build());
        opts.addOption(Option.builder()
                .hasArg()
                .argName("ms")
                .desc(rb.getString("idle-timeout"))
                .longOpt("idle-timeout")
                .build());
    }

    public static void hl7Service() {

        String[] args = {"-b", ""};
        ResourceBundle res = ResourceBundle.getBundle("ris");
        args[1]+=res.getString("hl7Port");
        System.out.println("-------------hl7服务参数："+args[1]);
        try {
            CommandLine cl = parseComandLine(args);
            HL7Rcv main = new HL7Rcv();
            configure(main, cl);
            ExecutorService executorService = Executors.newCachedThreadPool();
            ScheduledExecutorService scheduledExecutorService =
                    Executors.newSingleThreadScheduledExecutor();
            main.device.setScheduledExecutor(scheduledExecutorService);
            main.device.setExecutor(executorService);
            main.device.bindConnections();
        } catch (ParseException e) {
            System.err.println("hl7rcv: " + e.getMessage());
            System.err.println(rb.getString("try"));
            System.exit(2);
        } catch (Exception e) {
            System.err.println("hl7rcv: " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }
//    public static void main(String[] args) {
//        try {
//            CommandLine cl = parseComandLine(args);
//            HL7Rcv main = new HL7Rcv();
//            configure(main, cl);
//            ExecutorService executorService = Executors.newCachedThreadPool();
//            ScheduledExecutorService scheduledExecutorService =
//                    Executors.newSingleThreadScheduledExecutor();
//            main.device.setScheduledExecutor(scheduledExecutorService);
//            main.device.setExecutor(executorService);
//            main.device.bindConnections();
//        } catch (ParseException e) {
//            System.err.println("hl7rcv: " + e.getMessage());
//            System.err.println(rb.getString("try"));
//            System.exit(2);
//        } catch (Exception e) {
//            System.err.println("hl7rcv: " + e.getMessage());
//            e.printStackTrace();
//            System.exit(2);
//        }
//    }

    private static void configure(HL7Rcv main, CommandLine cl)
            throws Exception, MalformedURLException, ParseException,
            IOException {
        if (!cl.hasOption("ignore"))
            main.setStorageDirectory(
                    cl.getOptionValue("directory", "."));
        if (cl.hasOption("x")) {
            String s = cl.getOptionValue("x");
            main.setXSLT(new File(s).toURI().toURL());
            main.setXSLTParameters(cl.getOptionValues("xsl-param"));
        }
        main.setCharacterSet(cl.getOptionValue("charset"));
//        main.setCharacterSet("UTF-8");
        configureBindServer(main.conn, cl);
        CLIUtils.configure(main.conn, cl);
    }

    private static void configureBindServer(Connection conn, CommandLine cl)
            throws ParseException {
        if (!cl.hasOption("b"))
            throw new MissingOptionException(
                    CLIUtils.rb.getString("missing-bind-opt"));
        String aeAtHostPort = cl.getOptionValue("b");
        String[] hostAndPort = StringUtils.split(aeAtHostPort, ':');
        int portIndex = hostAndPort.length - 1;
        conn.setPort(Integer.parseInt(hostAndPort[portIndex]));
        if (portIndex > 0)
            conn.setHostname(hostAndPort[0]);
    }

    public static Message parse(String hl7Str) {

        // 解析器
        PipeParser parser = new PipeParser();
        // 解析
        Message message = null;
        try {
            message = parser.parse(hl7Str);
        } catch (ca.uhn.hl7v2.HL7Exception e) {
            e.printStackTrace();
        }
        //message.get("MSH") 返回这个段/结构
//        System.out.println(message.get("MSH").toString());//结构的名称
//        System.out.println(message.get("MSH").getMessage());//返回此结构所属的消息对象。
//        System.out.println(message.get("MSH").getName());//结构的名称
//        System.out.println(message.get("MSH").getParent());//结构的名称

        if (message instanceof ACK) {
            // 如果确认是ACK消息可以直接返回ACK
            return (ACK) message;
        }
        return message;
    }

    static void myPrint(String str) {
        System.out.println("----------" + str + "----------");
    }

    private void save2database(String hl7Str) {
        // 解析为消息
        ORM_O01 message = (ORM_O01) parse(hl7Str);

        //病人姓名
        ST name2 = message.getPATIENT().getPID().getPid5_PatientName()[0].getFamilyName().getSurname();
        ST name1 = message.getPATIENT().getPID().getPid5_PatientName()[0].getGivenName();
        String name = name1.toString() + name2.toString();

        //病人id == 住院号
//        String id = message.getPATIENT().getPID().getPid2_PatientID().getCx1_ID().toString();
//        String inpatientNumber = message.getPATIENT().getPID().getPid2_PatientID().getCx1_ID().toString();
        String id = message.getPATIENT().getPID().getPid2_PatientID().getCx1_IDNumber().toString();
        String inpatientNumber = message.getPATIENT().getPID().getPid2_PatientID().getCx1_IDNumber().toString();

        //姓名拼音
//        String namePinYin = message.getPATIENT().getPID().getPid3_PatientIdentifierList(0).getID().toString();
        String namePinYin = message.getPATIENT().getPID().getPid3_PatientIdentifierList(0).getCx1_IDNumber().toString();

        //证件类型
        String IDType = message.getPATIENT().getPID().getPid26_Citizenship(0).getCwe2_Text().toString();

        //证件号
        String IDNumber =message.getPATIENT().getPID().getPid20_DriverSLicenseNumberPatient().getDln1_LicenseNumber().toString();

        //住院科室/申请科室
        myPrint("住院科室/申请科室");
        System.out.println(message.getORDER().getORC().getOrc17_EnteringOrganization().getCwe2_Text());

        //住院床号
        String inpatientBedNumber = message.getPATIENT().getPATIENT_VISIT().getPV1().getPv13_AssignedPatientLocation().getPl2_Room().toString()
                + message.getPATIENT().getPATIENT_VISIT().getPV1().getPv13_AssignedPatientLocation().getPl3_Bed().toString();

        //医保类型
        String healthCareType = message.getPATIENT().getPATIENT_VISIT().getPV1().getPv120_FinancialClass(0).getFc1_FinancialClassCode().toString();

        //出生日期 //年龄类型 //年龄
        String birthDate = null;
        String ageType = null;
        String age = null;
        birthDate = message.getPATIENT().getPID().getPid7_DateTimeOfBirth().toString();
        Date birthData2 = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");//上边获取的日期类型
        try {
            birthData2 = sdf.parse(birthDate);//返回date格式
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Date currentDate = new Date(System.currentTimeMillis());//获得当前时间，Date类型
        int year = 0;
        int month = 0;
        int day = 0;
        year = (int) ((currentDate.getTime() - birthData2.getTime()) / (1000 * 60 * 60 * 24)) / 365; // 计算年
        month = (int) ((currentDate.getTime() - birthData2.getTime()) / (1000 * 60 * 60 * 24)) / 30; // 计算月
        day = (int) ((currentDate.getTime() - birthData2.getTime()) / (1000 * 60 * 60 * 24)); // 计算天
        if (year == 0) {//不满1岁
            if (month == 0) {//不满1个月
                ageType = "天";
                age = String.valueOf(day);
            } else {
                ageType = "月";
                age = String.valueOf(month);
            }
        } else {
            ageType = "年";
            age = String.valueOf(year);
        }
        //把现在日期格式和数据库已有日期格式保持一致
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        birthDate = formatter.format(birthData2);


        //性别
        String gender = message.getPATIENT().getPID().getPid8_AdministrativeSex().toString();

        //怀孕情况
        String pregnancy = null;
        myPrint("怀孕情况");
        if (message.getPATIENT().getPATIENT_VISIT().getPV1().getPv115_AmbulatoryStatus(0).toString().equals("B6")) {
            pregnancy = "是";
        } else {
            pregnancy = "否";
        }

        //手机号
        String phoneNumber = message.getPATIENT().getPID().getPid13_PhoneNumberHome(0).getXtn7_LocalNumber().toString();

        //地址
        String address = message.getPATIENT().getPID().getPid11_PatientAddress(0).getXad1_StreetAddress().getSad1_StreetOrMailingAddress().toString();

        //是否急诊 == 病人类别
        String patientType = null;
        String emergency = null;
        switch (message.getPATIENT().getPATIENT_VISIT().getPV1().getPv12_PatientClass().toString()) {
            case "I":
                patientType= "住院";
                emergency = "否";
                break;
            case "E":
                emergency = "是";
                patientType = "急诊";
                break;
            case "O":
                emergency = "否";
                patientType= "门诊";
                break;
            default:
                emergency = "否";
                patientType= "其他";
        }



        //临床医生工号
        String clinicianID = message.getPATIENT().getPATIENT_VISIT().getPV1().getPv17_AttendingDoctor(0).getXcn1_IDNumber().toString();
//        myPrint("临床医生姓名");
////        name1 = message.getORDER().getORC().getOrc12_OrderingProvider()[0].getGivenName();
////        name2 = message.getORDER().getORC().getOrc12_OrderingProvider()[0].getFamilyName().getSurname();
//        name1 = message.getPATIENT().getPATIENT_VISIT().getPV1().getPv17_AttendingDoctor(0).getXcn3_GivenName();
//        name2 = message.getPATIENT().getPATIENT_VISIT().getPV1().getPv17_AttendingDoctor(0).getXcn2_FamilyName().getSurname();
//        System.out.println(name1.toString() + name2.toString());

        //检查科室 == 住院科室
        String inpatientDepartment = message.getORDER().getORC().getOrc17_EnteringOrganization().getCwe2_Text().toString();
        String department = message.getORDER().getORC().getOrc17_EnteringOrganization().getCwe2_Text().toString();

        //检查位置
        String bodyParts = message.getORDER().getORDER_DETAIL().getOBR().getObr31_ReasonForStudy(0).getCwe1_Identifier().toString();

        //检查设备
        myPrint("检查设备[按道理说应该是需要登记员进行分配操作]");
        System.out.println(message.getPATIENT().getPATIENT_VISIT().getPV1().getPv139_ServicingFacility());

        //模式/设备类型
        String modality = message.getORDER().getORDER_DETAIL().getOBR().getObr24_DiagnosticServSectID().toString();

        //请求过程描述/检查原因
        String requestedProcedureDescription = message.getORDER().getORDER_DETAIL().getOBR().getObr31_ReasonForStudy(0).getCwe2_Text().toString();


        String accessionNumber = UUIDUtil.getUUID();
        String registrarID = null;
        //String studyDevice = request.getParameter("studyDevice");//此处得到的参数是设备id
        String studyDevice =null;
        String studyInstanceUID = UIDUtils.createUID();
//        //patient表
//        //studyinfo表
        Patient p = new Patient();
        StudyInfo s = new StudyInfo();
        p.setId(id);
        p.setName(name);
        p.setNamePinYin(namePinYin);
        p.setAge(age);
        p.setAgeType(ageType);
        p.setGender(gender);
        p.setBirthDate(birthDate);
        p.setAddress(address);
        p.setPregnancy(pregnancy);
        p.setInpatientDepartment(inpatientDepartment);
        p.setInpatientBedNumber(inpatientBedNumber);
        p.setInpatientNumber(inpatientNumber);
        p.setPhoneNumber(phoneNumber);
        p.setPatientType(patientType);
        p.setIDType(IDType);
        p.setIDNumber(IDNumber);
        p.setHealthCareType(healthCareType);
        s.setAccessionNumber(accessionNumber);
        s.setStatus("0");
//        s.setPatientID(id);//根据是否存在病人设置，若已存在病人则使用已存在的病人的id否则使用新生成的id
        s.setDepartment(department);
        s.setEmergency(emergency);
        s.setClinicianID(clinicianID);
        s.setRegistrarID(registrarID);
        s.setBodyParts(bodyParts);
        s.setModality(modality);
        s.setStudyDevice(studyDevice);
        s.setStudyInstanceUID(studyInstanceUID);
        s.setRequestedProcedureDescription(requestedProcedureDescription);
        RegisterService rs = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        boolean flag = rs.save(p, s);
    }

    private UnparsedHL7Message onMessage(UnparsedHL7Message msg)
            throws Exception {
//        System.setProperty(MllpConstants.CHARSET_KEY, "UTF-8");

//        String hl7Str = new String(new String(msg.data()).getBytes( "UTF-8"));
//        myPrint("字符集");
//        String encoding = System.getProperty("file.encoding");
//        System.out.println(encoding);
//        把接收到的申请保存到数据库
        save2database(new String(msg.data(),StandardCharsets.UTF_8));
//        myPrint("hl7Str 直接使用utf-8解析byte");
//        System.out.println(new String(msg.data(),StandardCharsets.UTF_8));
//        myPrint("hl7Str 原始 失败[原始是直接解码为GBK]");
//        System.out.println(new String(msg.data()));
//        myPrint("hl7Str 直接解码为utf-8 失败");
//        System.out.println(new String(new String(msg.data()).getBytes( "UTF-8")));
//        myPrint("hl7Str 先解码为utf-8，然后解码为GBK");
//        System.out.println(new String(new String(new String(msg.data()).getBytes( "UTF-8")).getBytes("GBK")));
//        myPrint("hl7Str 先解码为GBK，然后解码为utf-8");
//        System.out.println(new String(new String(new String(msg.data()).getBytes( "GBK")).getBytes("UTF-8")));
//        myPrint("hl7Str 通过ISO-8859-1解码");
//        System.out.println(new String(new String(msg.data()).getBytes( StandardCharsets.ISO_8859_1)));
//        myPrint("hl7Str 通过ISO-8859-1解码，然后再解码为utf-8");
//        System.out.println(new String(new String(new String(msg.data()).getBytes( StandardCharsets.ISO_8859_1)).getBytes(StandardCharsets.UTF_8)));
//
//        if (storageDir != null)
//            storeToFile(msg.data(), new File(
//                    new File(storageDir, msg.msh().getMessageType()),
//                    msg.msh().getField(9, "_NULL_")));
//        return new UnparsedHL7Message(tpls == null
//                ? HL7Message.makeACK(msg.msh(), HL7Exception.AA, null).getBytes(null)
//                : xslt(msg));
        if (storageDir != null)
            storeToFile(new String(msg.data(),StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8), new File(
                    new File(storageDir, msg.msh().getMessageType()),
                    msg.msh().getField(9, "_NULL_")));
        return new UnparsedHL7Message(tpls == null
                ? HL7Message.makeACK(msg.msh(), HL7Exception.AA, null).getBytes(null)
                : xslt(msg));
    }

    //获取到消息类型和XXX，组装成文件名，开始进行存储
    private void storeToFile(byte[] data, File f) throws IOException {
        Connection.LOG.info("M-WRITE {}", f);
        f.getParentFile().mkdirs();
        FileOutputStream out = new FileOutputStream(f);
        try {
            out.write(data);
        } finally {
            out.close();
        }
    }

    private byte[] xslt(UnparsedHL7Message msg)
            throws Exception {
        String charsetName = HL7Charset.toCharsetName(msg.msh().getField(17, charset));
//        String charsetName = HL7Charset.toCharsetName("UNICODE UTF-8");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TransformerHandler th = factory.newTransformerHandler(tpls);
        Transformer t = th.getTransformer();
        t.setParameter("MessageControlID", HL7Segment.nextMessageControlID());
        t.setParameter("DateTimeOfMessage", HL7Segment.timeStamp(new Date()));
        if (xsltParams != null)
            for (int i = 1; i < xsltParams.length; i++, i++)
                t.setParameter(xsltParams[i - 1], xsltParams[i]);
        th.setResult(new SAXResult(new HL7ContentHandler(
                new OutputStreamWriter(out, charsetName))));
        new HL7Parser(th).parse(new InputStreamReader(
                new ByteArrayInputStream(msg.data()),
                charsetName));
        return out.toByteArray();
    }

    public Device getDevice() {
        return device;
    }

    public Connection getConn() {
        return conn;
    }
}
