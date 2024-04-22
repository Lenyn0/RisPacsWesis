<a name="xrAFL"></a>
# 已经下载的版本
5.15.0<br />5.16.1<br />5.21.0<br />5.23.2【这个版本的工具有问题】

<a name="TcWfD"></a>
# 学习shell脚本，看bat脚本怎么使用的dcm4che工具的
是通过传参使用的，处理参数传参

<a name="R1Q34"></a>
# [agfa2dcm](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-agfa2dcm/README.md): Extract DICOM files from Agfa BLOB file
从 Agfa BLOB 文件中提取 DICOM 文件

AGFA（爱克发·吉华集团）主要业务分为三类，分别是印刷科技**影像、医疗**和材料科技。在印刷科技影像方面，集团制造模拟和数字印刷系统、软件及耗材，包括直接制版机系统和工业用喷墨打印机。

BLOB <br />计算机视觉中的Blob是指图像中的一块连通区域，Blob分析就是对前景/背景分离后的二值图像，进行连通域提取和标记。标记完成的每一个Blob都代表一个前景目标，然后就可以计算Blob的一些相关特征。其优点在于通过Blob提取，可以获得相关区域的信息，但是速度较慢，分析难度大。<br />BLOB <br />数据库的一种数据类型<br />BLOB <br />.blob后缀数据
<a name="9rbId"></a>
# [dcm2dcm](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-dcm2dcm/README.md): Transcode DICOM file according the specified Transfer Syntax
根据指定的传输语法对DICOM文件进行转码


进行压缩操作
<a name="fxRbJ"></a>
## 使用示例1
```
Examples:
$ .\dcm2dcm --jpll image.dcm jpll.dcm
Compress DICOM image in.dcm to jpll.dcm with JPEG Lossless,Non-Hierarchical, First-Order Prediction (Process 14 [Selection Value 1])Transfer Syntax

--jpll                    compress JPEG Lossless; equivalent to -t 1.2.840.10008.1.2.4.70

-t,--transfer-syntax <uid>   transcode sources to specified Transfer
                              Syntax. At default use Explicit VR Little
                              Endian
```
<a name="Q4VjK"></a>
## 结果
```
C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>.\dcm2dcm --jpll image.dcm jpll.dcm
09:46:16,948 DEBUG - Decompressor: org.dcm4che3.opencv.NativeImageReader
八月 09, 2021 9:46:16 上午 org.opencv.osgi.OpenCVNativeLoader init
信息: Successfully loaded OpenCV native library.
09:46:16,998 DEBUG - Compressor: org.dcm4che3.opencv.NativeJPEGImageWriter
八月 09, 2021 9:46:17 上午 org.opencv.osgi.OpenCVNativeLoader init
信息: Successfully loaded OpenCV native library.
09:46:17,042 DEBUG - Decompressed frame #1 in 30 ms, ratio 1:36.338036
09:46:17,108 DEBUG - Compressed frame #1 in 63 ms, ratio 5.1832857:1
image.dcm -> jpll.dcm

C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>
```
<a name="CzRVb"></a>
## 使用示例2
```
$ .\dcm2dcm jpll.dcm out.dcm
Decompress DICOM image jpll.dcm to out.dcm with Explicit VR Little Endian Transfer Syntax
```
<a name="CQvdg"></a>
## 结果
![image.png](https://cdn.nlark.com/yuque/0/2021/png/2869223/1628473795913-88a21eb7-28fd-47ab-85f1-c89910165348.png#averageHue=%23ccd6c4&height=68&id=zOndT&originHeight=91&originWidth=829&originalType=binary&ratio=1&rotation=0&showTitle=false&size=22758&status=done&style=none&title=&width=622)
<a name="Axh2E"></a>
# [dcm2jpg](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-dcm2jpg/README.md): Convert DICOM image to JPEG or other image formats
将DICOM图像转换为JPEG或其他图像格式
<a name="t5yzg"></a>
## 使用示例1
```
Example: .\dcm2jpg image.dcm img.jpg
=> Convert DICOM image 'img.dcm' to JPEG image 'img.jpg'
```
<a name="h7R7P"></a>
## 结果

<a name="mKaGo"></a>
# [dcm2json](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-dcm2json/README.md): Convert DICOM file in JSON presentation
在JSON表示中转换DICOM文件

<a name="3u1X4"></a>
## 使用示例1
```
$ .\dcm2json image2.dcm
Write JSON representation of DICOM file image.dcm to standard output,
including only a reference to the pixel data in image.dcm
```
<a name="ITq8U"></a>
## 结果
直接把json输出在控制台

会把DICOM文件表示成JSON形式<br />主要把头文件全部提出取来<br />并且把图像数据所在的偏移位置也表示出来了
```json
C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>.\dcm2json image2.dcm
{"00020001":{"vr":"OB","InlineBinary":"AAE="},"00020002":{"vr":"UI","Value":["1.2.840.10008.5.1.4.1.1.4"]},"00020003":{"vr":"UI","Value":["0.0.0.0.1.8811.2.19.20010413115754.12432"]},"00020010":{"vr":"UI","Value":["1.2.840.10008.1.2.1"]},"00020012":{"vr":"UI","Value":["0.0.0.0"]},"00020013":{"vr":"SH","Value":["NOTSPECIFIED"]},"00020016":{"vr":"AE","Value":["NOTSPECIFIED"]},"00080008":{"vr":"CS","Value":["ORIGINAL","PRIMARY","MPR"]},"00080016":{"vr":"UI","Value":["1.2.840.10008.5.1.4.1.1.4"]},"00080018":{"vr":"UI","Value":["0.0.0.0.1.8811.2.19.20010413115754.12432"]},"00080020":{"vr":"DA","Value":["20010316"]},"00080021":{"vr":"DA","Value":["20010316"]},"00080022":{"vr":"DA","Value":["20010316"]},"00080023":{"vr":"DA","Value":["20010323"]},"00080030":{"vr":"TM","Value":["143008"]},"00080031":{"vr":"TM","Value":["143414"]},"00080032":{"vr":"TM","Value":["143415"]},"00080033":{"vr":"TM","Value":["143021"]},"00080050":{"vr":"SH"},"00080060":{"vr":"CS","Value":["MR"]},"00080070":{"vr":"LO","Value":["GE Medical Systems"]},"00080080":{"vr":"LO","Value":[null]},"00080090":{"vr":"PN","Value":[null]},"00081010":{"vr":"SH","Value":["MRS1"]},"00081030":{"vr":"LO","Value":["BRAIN"]},"0008103E":{"vr":"LO","Value":["FSE PD AXIAL OBL"]},"00081050":{"vr":"PN","Value":[null]},"00081070":{"vr":"PN","Value":[{"Alphabetic":"EC"}]},"00081090":{"vr":"LO","Value":["SIGNA"]},"00100010":{"vr":"PN","Value":[null]},"00100020":{"vr":"LO","Value":["123565"]},"00100030":{"vr":"DA"},"00100040":{"vr":"CS","Value":["F"]},"00101010":{"vr":"AS","Value":["028Y"]},"00101030":{"vr":"DS","Value":["61.2350"]},"001021B0":{"vr":"LT"},"00180020":{"vr":"CS","Value":["SE"]},"00180021":{"vr":"CS","Value":["SK"]},"00180022":{"vr":"CS","Value":["SP"]},"00180023":{"vr":"CS","Value":["2D"]},"00180024":{"vr":"SH","Value":["fse"]},"00180050":{"vr":"DS","Value":["5.00000"]},"00180080":{"vr":"DS","Value":["2300.00"]},"00180081":{"vr":"DS","Value":["22.0000"]},"00180083":{"vr":"DS","Value":["1.00000"]},"00180084":{"vr":"DS","Value":["63.8615"]},"00180086":{"vr":"IS","Value":["1"]},"00180087":{"vr":"DS","Value":["1.50000"]},"00180088":{"vr":"DS","Value":["2.00000"]},"00180089":{"vr":"IS","Value":["256"]},"00180091":{"vr":"IS","Value":["8"]},"00180095":{"vr":"DS","Value":["31.2500"]},"00181020":{"vr":"LO","Value":["3"]},"00181030":{"vr":"LO","Value":["CLINICAL BRAIN"]},"00181088":{"vr":"IS","Value":["0"]},"00181090":{"vr":"IS","Value":["0"]},"00181094":{"vr":"IS","Value":["0"]},"00181100":{"vr":"DS","Value":["220.000"]},"00181250":{"vr":"SH","Value":["HEAD"]},"00181310":{"vr":"US","Value":[0,256,256,0]},"00181312":{"vr":"CS","Value":["ROW"]},"00181314":{"vr":"DS","Value":["90"]},"00181316":{"vr":"DS","Value":["0.0313309"]},"00185100":{"vr":"CS","Value":["HFS"]},"0020000D":{"vr":"UI","Value":["0.0.0.0.2.8811.20010413115754.12432"]},"0020000E":{"vr":"UI","Value":["0.0.0.0.3.8811.2.20010413115754.12432"]},"00200010":{"vr":"SH","Value":["8811"]},"00200011":{"vr":"IS","Value":["2"]},"00200012":{"vr":"IS","Value":["31762"]},"00200013":{"vr":"IS","Value":["19"]},"00200020":{"vr":"CS","Value":["L","PH"]},"00200030":{"vr":"DS","Value":["-110.500","-95.2063","52.1425"]},"00200032":{"vr":"DS","Value":["-110.500","-95.2063","52.1425"]},"00200035":{"vr":"DS","Value":["1.00000","0.00000","0.00000","0.00000","0.990960","0.134158"]},"00200037":{"vr":"DS","Value":["1.00000","0.00000","0.00000","0.00000","0.990960","0.134158"]},"00200052":{"vr":"UI","Value":["0.0.0.0.4.8811.2.20010413115754.12432"]},"00201002":{"vr":"IS","Value":["1"]},"00201040":{"vr":"LO","Value":["NA"]},"00201041":{"vr":"DS","Value":["66.9000"]},"00280002":{"vr":"US","Value":[1]},"00280004":{"vr":"CS","Value":["MONOCHROME2"]},"00280010":{"vr":"US","Value":[256]},"00280011":{"vr":"US","Value":[256]},"00280030":{"vr":"DS","Value":["0.859375","0.859375"]},"00280100":{"vr":"US","Value":[16]},"00280101":{"vr":"US","Value":[16]},"00280102":{"vr":"US","Value":[15]},"00280103":{"vr":"US","Value":[1]},"00280106":{"vr":"SS","Value":[0]},"00280107":{"vr":"SS","Value":[860]},"00280120":{"vr":"SS","Value":[0]},"00281050":{"vr":"DS","Value":["0"]},"00281051":{"vr":"DS","Value":["0"]},"00281052":{"vr":"DS","Value":["0"]},"00281053":{"vr":"DS","Value":["1"]},"00281054":{"vr":"LO","Value":["SIGNAL INTENSITY (UNITLESS)"]},"7FE00010":{"vr":"OW","BulkDataURI":"file:/C:/Users/admin/Desktop/dcm4che-5.23.2-bin/dcm4che-5.23.2/bin/image2.dcm?offset=1842&length=131072"}}
C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>
```



<a name="02Tyr"></a>
## 使用示例2
```
.\dcm2json --blk-file-dir=./tmp/pixeldata/blk#####.tmp image2.dcm
Write JSON representation of DICOM file image.dcm to standard output,including a reference to the extracted pixel data in file /tmp/pixeldata/blk#####.tmp

-d,--blk-file-dir <directory>   directory were files with extracted bulkdata are stored if the DICOM object is read from "standard input; if not specified, files are stored into the default temporary-file directory

```
把json写入文件
<a name="F0x6J"></a>
## 结果
失败


<a name="kQSzd"></a>
# [dcm2pdf](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-dcm2pdf/README.md): Extract encapsulated PDF, CDA or STL from DICOM file
从DICOM文件中提取封装的PDF、CDA或STL

由PDF，CDA，STL封装的DICOM才能使用这个方法转换回来
<a name="9MdF6"></a>
## 使用示例1
```
Example 1: .\dcm2pdf image2.dcm file.pdf
=> Convert Encapsulated PDF DICOM object to a pdf file.
```
<a name="KudmH"></a>
## 结果
```
C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>.\dcm2pdf image2.dcm file.pdf
10:04:01,105 INFO  - DICOM file image2.dcm with MR Image Storage SOP Class cannot be converted to file type PDF

C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>
```

<a name="JqBda"></a>
# [dcm2str](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-dcm2str/README.md): Apply Attributes Format Pattern to dicom file or command line parameters.

将属性格式模式应用于dicom文件或命令行参数

使用不成功，不清楚完成的效果是什么


```
Apply Attributes Format Pattern either to one or more DICOM files and/or
DICOM file(s) in one or more directories. If individual attributes are
specified, the attributes of the DICOM file(s) shall be overwritten by the
specified attributes before the Attributes Format Pattern is applied.
```
将属性格式模式应用于一个或多个目录中的一个或多个DICOM文件和/或DICOM文件。如果指定了单个属性，在应用属性格式模式之前，DICOM文件的属性应被指定属性覆盖。

```
Examples:
=> .\dcm2str -p '{0020000D,hash}\{0020000E,hash}\{00080018,hash}\{rnd}' image.dcm
Apply Attributes Format Pattern to the specified DICOM file.
```

将属性格式模式应用于指定的DICOM文件。

```
=> dcm2str -p '{0020000D,hash}/{0020000E,hash}/{00080018,hash}/{rnd}' -sStudyInstanceUID=1.2.3 -sSeriesInstanceUID=1.2.3.4 -sSOPInstanceUID=1.2.3.4.5 -- image.dcm
Overwrite attributes of the specified DICOM file with specified DICOM
attributes and then apply Attributes Format Pattern to the specified DICOM
file.
```
使用指定的DICOM属性覆盖指定DICOM文件的属性，然后将属性格式模式应用于指定的DICOM文件。

```
=> dcm2str -p '{0020000D,hash}/{0020000E,hash}/{00080018,hash}/{rnd}' -sStudyInstanceUID=1.2.3 -sSeriesInstanceUID=1.2.3.4 -sSOPInstanceUID=1.2.3.4.5 -- image.dcm /path-to-other-DICOM-files-directory
Overwrite attributes of the specified DICOM file and of other DICOM files
in the directory with the specified DICOM attributes and only then apply
Attributes Format Pattern to DICOM file and to other DICOM files in the
directory.
```
使用指定的 DICOM 属性覆盖指定 DICOM 文件和目录中其他 DICOM 文件的属性，然后才将属性格式模式应用于 DICOM 文件和目录中的其他 DICOM 文件。

```
=> dcm2str -p '{0020000D,hash}/{0020000E,hash}/{00080018,hash}/{rnd}' image.dcm /path-to-other-DICOM-files-directory
Apply Attributes Format Pattern to the specified DICOM file and also to
the other DICOM files in the specified directory.
```

```
=> .\dcm2str -p '{0020000D,hash}/{0020000E,hash}/{00080018,hash}/{rnd}' -s StudyInstanceUID=1.2.3 -s SeriesInstanceUID=1.2.3.4 -s SOPInstanceUID=1.2.3.4.5
Apply Attributes Format Pattern to DICOM attributes passed as command line
parameters.
```

<a name="LUDm2"></a>
## 使用示例1


<a name="iSUVE"></a>
## 结果
使用不成功



<a name="3IR5R"></a>
# [dcm2xml](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-dcm2xml/README.md): Convert DICOM file in XML presentation
在XML表示中转换DICOM文件

<a name="XFE48"></a>
## 使用示例1 把DCIOM文件转换成XML形式
```
Examples:
$ .\dcm2xml image.dcm
Write XML representation of DICOM file image.dcm to standard output,
including only a reference to the pixel data in image.dcm
```

<a name="tqd02"></a>
## 结果
把dicom文件头和数据位置信息变成xml格式和json比较类似<br />是输出在控制台
```xml

C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>.\dcm2xml image.dcm
<?xml version="1.0" encoding="UTF-8"?><NativeDicomModel xml:space="preserve"><DicomAttribute keyword="FileMetaInformationVersion" tag="00020001" vr="OB"><InlineBinary>AAE=</InlineBinary></DicomAttribute><DicomAttribute keyword="MediaStorageSOPClassUID" tag="00020002" vr="UI"><Value number="1">1.2.840.10008.5.1.4.1.1.4</Value></DicomAttribute><DicomAttribute keyword="MediaStorageSOPInstanceUID" tag="00020003" vr="UI"><Value number="1">0.0.0.0.1.8811.2.19.20010413115754.12432</Value></DicomAttribute><DicomAttribute keyword="TransferSyntaxUID" tag="00020010" vr="UI"><Value number="1">1.2.840.10008.1.2.1</Value></DicomAttribute><DicomAttribute keyword="ImplementationClassUID" tag="00020012" vr="UI"><Value number="1">0.0.0.0</Value></DicomAttribute><DicomAttribute keyword="ImplementationVersionName" tag="00020013" vr="SH"><Value number="1">NOTSPECIFIED</Value></DicomAttribute><DicomAttribute keyword="SourceApplicationEntityTitle" tag="00020016" vr="AE"><Value number="1">NOTSPECIFIED</Value></DicomAttribute><DicomAttribute keyword="ImageType" tag="00080008" vr="CS"><Value number="1">ORIGINAL</Value><Value number="2">PRIMARY</Value><Value number="3">MPR</Value></DicomAttribute><DicomAttribute keyword="SOPClassUID" tag="00080016" vr="UI"><Value number="1">1.2.840.10008.5.1.4.1.1.4</Value></DicomAttribute><DicomAttribute keyword="SOPInstanceUID" tag="00080018" vr="UI"><Value number="1">0.0.0.0.1.8811.2.19.20010413115754.12432</Value></DicomAttribute><DicomAttribute keyword="StudyDate" tag="00080020" vr="DA"><Value number="1">20010316</Value></DicomAttribute><DicomAttribute keyword="SeriesDate" tag="00080021" vr="DA"><Value number="1">20010316</Value></DicomAttribute><DicomAttribute keyword="AcquisitionDate" tag="00080022" vr="DA"><Value number="1">20010316</Value></DicomAttribute><DicomAttribute keyword="ContentDate" tag="00080023" vr="DA"><Value number="1">20010323</Value></DicomAttribute><DicomAttribute keyword="StudyTime" tag="00080030" vr="TM"><Value number="1">143008</Value></DicomAttribute><DicomAttribute keyword="SeriesTime" tag="00080031" vr="TM"><Value number="1">143414</Value></DicomAttribute><DicomAttribute keyword="AcquisitionTime" tag="00080032" vr="TM"><Value number="1">143415</Value></DicomAttribute><DicomAttribute keyword="ContentTime" tag="00080033" vr="TM"><Value number="1">143021</Value></DicomAttribute><DicomAttribute keyword="AccessionNumber" tag="00080050" vr="SH"/><DicomAttribute keyword="Modality" tag="00080060" vr="CS"><Value number="1">MR</Value></DicomAttribute><DicomAttribute keyword="Manufacturer" tag="00080070" vr="LO"><Value number="1">GE Medical Systems</Value></DicomAttribute><DicomAttribute keyword="InstitutionName" tag="00080080" vr="LO"><Value number="1"/></DicomAttribute><DicomAttribute keyword="ReferringPhysicianName" tag="00080090" vr="PN"><PersonName number="1"/></DicomAttribute><DicomAttribute keyword="StationName" tag="00081010" vr="SH"><Value number="1">MRS1</Value></DicomAttribute><DicomAttribute keyword="StudyDescription" tag="00081030" vr="LO"><Value number="1">BRAIN</Value></DicomAttribute><DicomAttribute keyword="SeriesDescription" tag="0008103E" vr="LO"><Value number="1">FSE PD AXIAL OBL</Value></DicomAttribute><DicomAttribute keyword="PerformingPhysicianName" tag="00081050" vr="PN"><PersonName number="1"/></DicomAttribute><DicomAttribute keyword="OperatorsName" tag="00081070" vr="PN"><PersonName number="1"><Alphabetic><FamilyName>EC</FamilyName></Alphabetic></PersonName></DicomAttribute><DicomAttribute keyword="ManufacturerModelName" tag="00081090" vr="LO"><Value number="1">SIGNA</Value></DicomAttribute><DicomAttribute keyword="PatientName" tag="00100010" vr="PN"><PersonName number="1"/></DicomAttribute><DicomAttribute keyword="PatientID" tag="00100020" vr="LO"><Value number="1">123565</Value></DicomAttribute><DicomAttribute keyword="PatientBirthDate" tag="00100030" vr="DA"/><DicomAttribute keyword="PatientSex" tag="00100040" vr="CS"><Value number="1">F</Value></DicomAttribute><DicomAttribute keyword="PatientAge" tag="00101010" vr="AS"><Value number="1">028Y</Value></DicomAttribute><DicomAttribute keyword="PatientWeight" tag="00101030" vr="DS"><Value number="1">61.2350</Value></DicomAttribute><DicomAttribute keyword="AdditionalPatientHistory" tag="001021B0" vr="LT"/><DicomAttribute keyword="ScanningSequence" tag="00180020" vr="CS"><Value number="1">SE</Value></DicomAttribute><DicomAttribute keyword="SequenceVariant" tag="00180021" vr="CS"><Value number="1">SK</Value></DicomAttribute><DicomAttribute keyword="ScanOptions" tag="00180022" vr="CS"><Value number="1">SP</Value></DicomAttribute><DicomAttribute keyword="MRAcquisitionType" tag="00180023" vr="CS"><Value number="1">2D</Value></DicomAttribute><DicomAttribute keyword="SequenceName" tag="00180024" vr="SH"><Value number="1">fse</Value></DicomAttribute><DicomAttribute keyword="SliceThickness" tag="00180050" vr="DS"><Value number="1">5.00000</Value></DicomAttribute><DicomAttribute keyword="RepetitionTime" tag="00180080" vr="DS"><Value number="1">2300.00</Value></DicomAttribute><DicomAttribute keyword="EchoTime" tag="00180081" vr="DS"><Value number="1">22.0000</Value></DicomAttribute><DicomAttribute keyword="NumberOfAverages" tag="00180083" vr="DS"><Value number="1">1.00000</Value></DicomAttribute><DicomAttribute keyword="ImagingFrequency" tag="00180084" vr="DS"><Value number="1">63.8615</Value></DicomAttribute><DicomAttribute keyword="EchoNumbers" tag="00180086" vr="IS"><Value number="1">1</Value></DicomAttribute><DicomAttribute keyword="MagneticFieldStrength" tag="00180087" vr="DS"><Value number="1">1.50000</Value></DicomAttribute><DicomAttribute keyword="SpacingBetweenSlices" tag="00180088" vr="DS"><Value number="1">2.00000</Value></DicomAttribute><DicomAttribute keyword="NumberOfPhaseEncodingSteps" tag="00180089" vr="IS"><Value number="1">256</Value></DicomAttribute><DicomAttribute keyword="EchoTrainLength" tag="00180091" vr="IS"><Value number="1">8</Value></DicomAttribute><DicomAttribute keyword="PixelBandwidth" tag="00180095" vr="DS"><Value number="1">31.2500</Value></DicomAttribute><DicomAttribute keyword="SoftwareVersions" tag="00181020" vr="LO"><Value number="1">3</Value></DicomAttribute><DicomAttribute keyword="ProtocolName" tag="00181030" vr="LO"><Value number="1">CLINICAL BRAIN</Value></DicomAttribute><DicomAttribute keyword="HeartRate" tag="00181088" vr="IS"><Value number="1">0</Value></DicomAttribute><DicomAttribute keyword="CardiacNumberOfImages" tag="00181090" vr="IS"><Value number="1">0</Value></DicomAttribute><DicomAttribute keyword="TriggerWindow" tag="00181094" vr="IS"><Value number="1">0</Value></DicomAttribute><DicomAttribute keyword="ReconstructionDiameter" tag="00181100" vr="DS"><Value number="1">220.000</Value></DicomAttribute><DicomAttribute keyword="ReceiveCoilName" tag="00181250" vr="SH"><Value number="1">HEAD</Value></DicomAttribute><DicomAttribute keyword="AcquisitionMatrix" tag="00181310" vr="US"><Value number="1">0</Value><Value number="2">256</Value><Value number="3">256</Value><Value number="4">0</Value></DicomAttribute><DicomAttribute keyword="InPlanePhaseEncodingDirection" tag="00181312" vr="CS"><Value number="1">ROW</Value></DicomAttribute><DicomAttribute keyword="FlipAngle" tag="00181314" vr="DS"><Value number="1">90</Value></DicomAttribute><DicomAttribute keyword="SAR" tag="00181316" vr="DS"><Value number="1">0.0313309</Value></DicomAttribute><DicomAttribute keyword="PatientPosition" tag="00185100" vr="CS"><Value number="1">HFS</Value></DicomAttribute><DicomAttribute keyword="StudyInstanceUID" tag="0020000D" vr="UI"><Value number="1">0.0.0.0.2.8811.20010413115754.12432</Value></DicomAttribute><DicomAttribute keyword="SeriesInstanceUID" tag="0020000E" vr="UI"><Value number="1">0.0.0.0.3.8811.2.20010413115754.12432</Value></DicomAttribute><DicomAttribute keyword="StudyID" tag="00200010" vr="SH"><Value number="1">8811</Value></DicomAttribute><DicomAttribute keyword="SeriesNumber" tag="00200011" vr="IS"><Value number="1">2</Value></DicomAttribute><DicomAttribute keyword="AcquisitionNumber" tag="00200012" vr="IS"><Value number="1">31762</Value></DicomAttribute><DicomAttribute keyword="InstanceNumber" tag="00200013" vr="IS"><Value number="1">19</Value></DicomAttribute><DicomAttribute keyword="PatientOrientation" tag="00200020" vr="CS"><Value number="1">L</Value><Value number="2">PH</Value></DicomAttribute><DicomAttribute keyword="ImagePosition" tag="00200030" vr="DS"><Value number="1">-110.500</Value><Value number="2">-95.2063</Value><Value number="3">52.1425</Value></DicomAttribute><DicomAttribute keyword="ImagePositionPatient" tag="00200032" vr="DS"><Value number="1">-110.500</Value><Value number="2">-95.2063</Value><Value number="3">52.1425</Value></DicomAttribute><DicomAttribute keyword="ImageOrientation" tag="00200035" vr="DS"><Value number="1">1.00000</Value><Value number="2">0.00000</Value><Value number="3">0.00000</Value><Value number="4">0.00000</Value><Value number="5">0.990960</Value><Value number="6">0.134158</Value></DicomAttribute><DicomAttribute keyword="ImageOrientationPatient" tag="00200037" vr="DS"><Value number="1">1.00000</Value><Value number="2">0.00000</Value><Value number="3">0.00000</Value><Value number="4">0.00000</Value><Value number="5">0.990960</Value><Value number="6">0.134158</Value></DicomAttribute><DicomAttribute keyword="FrameOfReferenceUID" tag="00200052" vr="UI"><Value number="1">0.0.0.0.4.8811.2.20010413115754.12432</Value></DicomAttribute><DicomAttribute keyword="ImagesInAcquisition" tag="00201002" vr="IS"><Value number="1">1</Value></DicomAttribute><DicomAttribute keyword="PositionReferenceIndicator" tag="00201040" vr="LO"><Value number="1">NA</Value></DicomAttribute><DicomAttribute keyword="SliceLocation" tag="00201041" vr="DS"><Value number="1">66.9000</Value></DicomAttribute><DicomAttribute keyword="SamplesPerPixel" tag="00280002" vr="US"><Value number="1">1</Value></DicomAttribute><DicomAttribute keyword="PhotometricInterpretation" tag="00280004" vr="CS"><Value number="1">MONOCHROME2</Value></DicomAttribute><DicomAttribute keyword="Rows" tag="00280010" vr="US"><Value number="1">256</Value></DicomAttribute><DicomAttribute keyword="Columns" tag="00280011" vr="US"><Value number="1">256</Value></DicomAttribute><DicomAttribute keyword="PixelSpacing" tag="00280030" vr="DS"><Value number="1">0.859375</Value><Value number="2">0.859375</Value></DicomAttribute><DicomAttribute keyword="BitsAllocated" tag="00280100" vr="US"><Value number="1">16</Value></DicomAttribute><DicomAttribute keyword="BitsStored" tag="00280101" vr="US"><Value number="1">16</Value></DicomAttribute><DicomAttribute keyword="HighBit" tag="00280102" vr="US"><Value number="1">15</Value></DicomAttribute><DicomAttribute keyword="PixelRepresentation" tag="00280103" vr="US"><Value number="1">1</Value></DicomAttribute><DicomAttribute keyword="SmallestImagePixelValue" tag="00280106" vr="SS"><Value number="1">0</Value></DicomAttribute><DicomAttribute keyword="LargestImagePixelValue" tag="00280107" vr="SS"><Value number="1">860</Value></DicomAttribute><DicomAttribute keyword="PixelPaddingValue" tag="00280120" vr="SS"><Value number="1">0</Value></DicomAttribute><DicomAttribute keyword="WindowCenter" tag="00281050" vr="DS"><Value number="1">0</Value></DicomAttribute><DicomAttribute keyword="WindowWidth" tag="00281051" vr="DS"><Value number="1">0</Value></DicomAttribute><DicomAttribute keyword="RescaleIntercept" tag="00281052" vr="DS"><Value number="1">0</Value></DicomAttribute><DicomAttribute keyword="RescaleSlope" tag="00281053" vr="DS"><Value number="1">1</Value></DicomAttribute><DicomAttribute keyword="RescaleType" tag="00281054" vr="LO"><Value number="1">SIGNAL INTENSITY (UNITLESS)</Value></DicomAttribute><DicomAttribute keyword="PixelData" tag="7FE00010" vr="OW"><BulkData uri="file:/C:/Users/admin/Desktop/dcm4che-5.23.2-bin/dcm4che-5.23.2/bin/image.dcm?offset=1842&amp;length=131072"/></DicomAttribute></NativeDicomModel>
C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>
```




<a name="I0s2F"></a>
# [dcmdir](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-dcmdir/README.md): Dump, create or update DICOMDIR file
转储、创建或更新DICOMDIR文件

<a name="NICYg"></a>
## 示例1 查看DICOMDIR内容
```
Examples:
$ dcmdir -l /media/cdrom/DICOMDIR
$ dcmdir -l DICOMDIR
$ dcmdir -l disk99/DICOMDIR
$ dcmdir -l series-000001/DICOMDIR


list content of DICOMDIR to stdout

 -l <dicomdir>                             list content of directory file <dicomdir> to standard out preserve encoding of sequence length from the original file
    --orig-seq-len                         preserve encoding of sequence
                                           length from the original file
```

(0004,1500) CS [SCSRUSS] ReferencedFileID<br />字段表示DICOMDIR同目录下含有的目录，用来存放DICOM图像<br />示例<br />(0004,1500) CS [571e6309\366d49c5\60ab0084\625feba9] ReferencedFileID<br />该DICOMDIR文件同目录下有571e6309目录，该目录下有366d49c5目录，366d49c5目录下有60ab0084目录，60ab0084目录下有最终文件625feba9

<a name="jf5E2"></a>
## 结果
输出DICOMDIR文件内记录的病人，检查，序列，图像信息<br />第1个病人信息<br />第1个病人第1个检查信息<br />第1个病人第1个检查第1个序列信息<br />第1个病人第1个检查第1个序列第1个图像信息<br />。<br />。<br />第2个病人信息<br />。<br />。
```
C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>dcmdir -l disk99/DICOMDIR
File Meta Information:
(0002,0001) OB [0\1] FileMetaInformationVersion
(0002,0002) UI [1.2.840.10008.1.3.10] MediaStorageSOPClassUID
(0002,0003) UI [2.25.91109157623178688842163747414651169184] MediaStorageSOPIn
(0002,0010) UI [1.2.840.10008.1.2.1] TransferSyntaxUID
(0002,0012) UI [1.2.40.0.13.1.3] ImplementationClassUID
(0002,0013) SH [dcm4che-5.23.2] ImplementationVersionName

File-set Information:
(0004,1130) CS [DISK99] FileSetID
(0004,1141) CS [README] FileSetDescriptorFileID
(0004,1200) UL [386] OffsetOfTheFirstDirectoryRecordOfTheRootDirectoryEntity
(0004,1202) UL [386] OffsetOfTheLastDirectoryRecordOfTheRootDirectoryEntity
(0004,1212) US [0] FileSetConsistencyFlag

1. PATIENT:
(0004,1400) UL [0] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [480] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [PATIENT] DirectoryRecordType
(0010,0010) PN [DOE^J1] PatientName
(0010,0020) LO [583295] PatientID

1.1. STUDY:
(0004,1400) UL [0] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [648] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [STUDY] DirectoryRecordType
(0008,0020) DA [20170716] StudyDate
(0008,0030) TM [154736] StudyTime
(0008,0050) SH [MOF5026] AccessionNumber
(0008,1030) LO [LEFT WRIST] StudyDescription
(0020,000D) UI [1.113654.3.13.1026] StudyInstanceUID
(0020,0010) SH [RP1026] StudyID

1.1.1. SERIES:
(0004,1400) UL [0] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [760] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [SERIES] DirectoryRecordType
(0008,0060) CS [MR] Modality
(0020,000E) UI [1.113654.5.14.1035] SeriesInstanceUID
(0020,0011) IS [107] SeriesNumber

1.1.1.1. IMAGE:
(0004,1400) UL [942] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\MR01.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [1.113654.5.15.1504] ReferencedSOPInstanceUIDInFile
(0004,1512) UI [1.2.840.10008.1.2] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [1] InstanceNumber

1.1.1.2. IMAGE:
(0004,1400) UL [1124] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\MR02.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [1.113654.5.15.1512] ReferencedSOPInstanceUIDInFile
(0004,1512) UI [1.2.840.10008.1.2] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [2] InstanceNumber

1.1.1.3. IMAGE:
(0004,1400) UL [1306] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\MR03.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [1.113654.5.15.1513] ReferencedSOPInstanceUIDInFile
(0004,1512) UI [1.2.840.10008.1.2] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [3] InstanceNumber

1.1.1.4. IMAGE:
(0004,1400) UL [1488] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\MR04.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [1.113654.5.15.1514] ReferencedSOPInstanceUIDInFile
(0004,1512) UI [1.2.840.10008.1.2] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [4] InstanceNumber

1.1.1.5. IMAGE:
(0004,1400) UL [1670] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\MR05.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [1.113654.5.15.1515] ReferencedSOPInstanceUIDInFile
(0004,1512) UI [1.2.840.10008.1.2] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [5] InstanceNumber

1.1.1.6. IMAGE:
(0004,1400) UL [1852] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\MR06.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [1.113654.5.15.1516] ReferencedSOPInstanceUIDInFile
(0004,1512) UI [1.2.840.10008.1.2] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [6] InstanceNumber

1.1.1.7. IMAGE:
(0004,1400) UL [2034] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\MR07.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [1.113654.5.15.1517] ReferencedSOPInstanceUIDInFile
(0004,1512) UI [1.2.840.10008.1.2] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [7] InstanceNumber

1.1.1.8. IMAGE:
(0004,1400) UL [2216] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\MR08.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [1.113654.5.15.1518] ReferencedSOPInstanceUIDInFile
(0004,1512) UI [1.2.840.10008.1.2] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [8] InstanceNumber

1.1.1.9. IMAGE:
(0004,1400) UL [2398] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\MR09.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [1.113654.5.15.1519] ReferencedSOPInstanceUIDInFile
(0004,1512) UI [1.2.840.10008.1.2] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [9] InstanceNumber

1.1.1.10. IMAGE:
(0004,1400) UL [2580] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\MR10.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [1.113654.5.15.1505] ReferencedSOPInstanceUIDInFile
(0004,1512) UI [1.2.840.10008.1.2] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [10] InstanceNumber

1.1.1.11. IMAGE:
(0004,1400) UL [2762] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\MR11.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [1.113654.5.15.1506] ReferencedSOPInstanceUIDInFile
(0004,1512) UI [1.2.840.10008.1.2] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [11] InstanceNumber

1.1.1.12. IMAGE:
(0004,1400) UL [2944] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\MR12.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [1.113654.5.15.1507] ReferencedSOPInstanceUIDInFile
(0004,1512) UI [1.2.840.10008.1.2] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [12] InstanceNumber

1.1.1.13. IMAGE:
(0004,1400) UL [3126] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\MR13.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [1.113654.5.15.1508] ReferencedSOPInstanceUIDInFile
(0004,1512) UI [1.2.840.10008.1.2] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [13] InstanceNumber

1.1.1.14. IMAGE:
(0004,1400) UL [3308] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\MR14.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [1.113654.5.15.1509] ReferencedSOPInstanceUIDInFile
(0004,1512) UI [1.2.840.10008.1.2] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [14] InstanceNumber

1.1.1.15. IMAGE:
(0004,1400) UL [3490] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\MR15.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [1.113654.5.15.1510] ReferencedSOPInstanceUIDInFile
(0004,1512) UI [1.2.840.10008.1.2] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [15] InstanceNumber

1.1.1.16. IMAGE:
(0004,1400) UL [0] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\MR16.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [1.113654.5.15.1511] ReferencedSOPInstanceUIDInFile
(0004,1512) UI [1.2.840.10008.1.2] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [16] InstanceNumber


C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>
```
<a name="Xrvt7"></a>
## 示例2 生成DICOMDIR文件
```
$ dcmdir -c disk99/DICOMDIR --fs-id DISK99 --fs-desc disk99/README disk99/DICOM


dcmdir -c disk99/DICOMDIR --fs-id DISK99 --fs-desc disk99/README disk99/DICOM


dcmdir -c images/DICOMDIR --fs-id images --fs-desc images/README images/DICOM

dcmdir -c path/to/DICOMDIR --fs-id images --fs-desc path/to/README path/to/study


C:\Users\admin\Desktop\dcm4che-5.21.0-bin\dcm4che-5.21.0\bin\DICOM


   --fs-desc <txtfile>                    specify File-set Descriptor File 指定文件集描述符文件
   
   --fs-id DISK99
   
   --fs-id <id>                           specify File-set ID
   
    -c <dicomdir>                       create new directory file <dicomdir> with references to DICOM files specified by file.. or directory.. arguments



create a new directory file with specified File-set ID and Descriptor File, referencing all DICOM Files in directory disk99/DICOM
```



<a name="y0NLH"></a>
## 结果
```

C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>$ dcmdir -c disk99/DICOMDIR --fs-id DISK99 --fs-desc disk99/README disk99/DICOM
'$' 不是内部或外部命令，也不是可运行的程序
或批处理文件。

C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>dcmdir -c disk99/DICOMDIR --fs-id DISK99 --fs-desc disk99/README disk99/DICOM
10:25:35,994 INFO  - M-UPDATE disk99\DICOMDIR: add PATIENT Record
10:25:35,995 INFO  - M-UPDATE disk99\DICOMDIR: add STUDY Record
10:25:35,996 INFO  - M-UPDATE disk99\DICOMDIR: add SERIES Record
10:25:35,996 INFO  - M-UPDATE disk99\DICOMDIR: add IMAGE Record
.
added 4 directory records to directory file disk99\DICOMDIR in 738 ms

C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>
```
查看生成的DICOMDIR内容
```

C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>dcmdir -l disk99/DICOMDIR
File Meta Information:
(0002,0001) OB [0\1] FileMetaInformationVersion
(0002,0002) UI [1.2.840.10008.1.3.10] MediaStorageSOPClassUID
(0002,0003) UI [2.25.209531851892034409512201092980660203807] MediaStorageSOPI
(0002,0010) UI [1.2.840.10008.1.2.1] TransferSyntaxUID
(0002,0012) UI [1.2.40.0.13.1.3] ImplementationClassUID
(0002,0013) SH [dcm4che-5.23.2] ImplementationVersionName

File-set Information:
(0004,1130) CS [DISK99] FileSetID
(0004,1141) CS [README] FileSetDescriptorFileID
(0004,1200) UL [386] OffsetOfTheFirstDirectoryRecordOfTheRootDirectoryEntity
(0004,1202) UL [386] OffsetOfTheLastDirectoryRecordOfTheRootDirectoryEntity
(0004,1212) US [0] FileSetConsistencyFlag

1. PATIENT:
(0004,1400) UL [0] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [488] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [PATIENT] DirectoryRecordType
(0010,0010) PN [] PatientName
(0010,0020) LO [123565] PatientID

1.1. STUDY:
(0004,1400) UL [0] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [660] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [STUDY] DirectoryRecordType
(0008,0020) DA [20010316] StudyDate
(0008,0030) TM [143008] StudyTime
(0008,0050) SH [] AccessionNumber
(0008,1030) LO [BRAIN] StudyDescription
(0020,000D) UI [0.0.0.0.2.8811.20010413115754.12432] StudyInstanceUID
(0020,0010) SH [8811] StudyID

1.1.1. SERIES:
(0004,1400) UL [0] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [790] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [SERIES] DirectoryRecordType
(0008,0060) CS [MR] Modality
(0020,000E) UI [0.0.0.0.3.8811.2.20010413115754.12432] SeriesInstanceUID
(0020,0011) IS [2] SeriesNumber

1.1.1.1. IMAGE:
(0004,1400) UL [0] OffsetOfTheNextDirectoryRecord
(0004,1410) US [65535] RecordInUseFlag
(0004,1420) UL [0] OffsetOfReferencedLowerLevelDirectoryEntity
(0004,1430) CS [IMAGE] DirectoryRecordType
(0004,1500) CS [DICOM\image.dcm] ReferencedFileID
(0004,1510) UI [1.2.840.10008.5.1.4.1.1.4] ReferencedSOPClassUIDInFile
(0004,1511) UI [0.0.0.0.1.8811.2.19.20010413115754.12432] ReferencedSOPInstanc
(0004,1512) UI [1.2.840.10008.1.2.1] ReferencedTransferSyntaxUIDInFile
(0020,0013) IS [19] InstanceNumber


C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>
```

<a name="LKjQZ"></a>
## 示例3 添加新的信息到已存在的DICOMDIR文件中
```
$ dcmdir -u disk99/DICOMDIR disk99/DICOM/CT1
add directory records referencing all DICOM files in directory disk99/DICOM/CT1 to existing directory file
```


<a name="YFzwT"></a>
## 结果

<a name="hHNUp"></a>
## 示例3 删除DICOMDIR文件里XX文件夹下的信息
```
$ dcmdir -d disk99/DICOMDIR disk99/DICOM/CT2
delete/deactivate directory records referencing DICOM files in directory disk99/DICOM/CT2
```


示例4
```
$ dcmdir -p disk99/DICOMDIR
delete/deactivate directory records without child records referencing any DICOM file
```

<a name="RcyWJ"></a>
# [dcmdump](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-dcmdump/README.md): Dump DICOM file in textual form

<a name="EA3Cy"></a>
## 示例1 转储/输出DICOM文件的信息
```
$ dcmdump image.dcm
Dump DICOM file image.dcm to standard output
```



<a name="tY1sF"></a>
## 结果
```

C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>dcmdump image.dcm
0: [0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\
132: (0002,0000) UL #4 [180] FileMetaInformationGroupLength
144: (0002,0001) OB #2 [0\1] FileMetaInformationVersion
158: (0002,0002) UI #26 [1.2.840.10008.5.1.4.1.1.4] MediaStorageSOPClassUID
192: (0002,0003) UI #40 [0.0.0.0.1.8811.2.19.20010413115754.12432] MediaStorag
240: (0002,0010) UI #20 [1.2.840.10008.1.2.1] TransferSyntaxUID
268: (0002,0012) UI #8 [0.0.0.0] ImplementationClassUID
284: (0002,0013) SH #12 [NOTSPECIFIED] ImplementationVersionName
304: (0002,0016) AE #12 [NOTSPECIFIED] SourceApplicationEntityTitle
324: (0008,0008) CS #20 [ORIGINAL\PRIMARY\MPR] ImageType
352: (0008,0016) UI #26 [1.2.840.10008.5.1.4.1.1.4] SOPClassUID
386: (0008,0018) UI #40 [0.0.0.0.1.8811.2.19.20010413115754.12432] SOPInstance
434: (0008,0020) DA #8 [20010316] StudyDate
450: (0008,0021) DA #8 [20010316] SeriesDate
466: (0008,0022) DA #8 [20010316] AcquisitionDate
482: (0008,0023) DA #8 [20010323] ContentDate
498: (0008,0030) TM #6 [143008] StudyTime
512: (0008,0031) TM #6 [143414] SeriesTime
526: (0008,0032) TM #6 [143415] AcquisitionTime
540: (0008,0033) TM #6 [143021] ContentTime
554: (0008,0050) SH #0 [] AccessionNumber
562: (0008,0060) CS #2 [MR] Modality
572: (0008,0070) LO #18 [GE Medical Systems] Manufacturer
598: (0008,0080) LO #28 [] InstitutionName
634: (0008,0090) PN #4 [] ReferringPhysicianName
646: (0008,1010) SH #4 [MRS1] StationName
658: (0008,1030) LO #6 [BRAIN] StudyDescription
672: (0008,103E) LO #16 [FSE PD AXIAL OBL] SeriesDescription
696: (0008,1050) PN #6 [] PerformingPhysicianName
710: (0008,1070) PN #2 [EC] OperatorsName
720: (0008,1090) LO #6 [SIGNA] ManufacturerModelName
734: (0010,0010) PN #14 [] PatientName
756: (0010,0020) LO #6 [123565] PatientID
770: (0010,0030) DA #0 [] PatientBirthDate
778: (0010,0040) CS #2 [F] PatientSex
788: (0010,1010) AS #4 [028Y] PatientAge
800: (0010,1030) DS #8 [61.2350] PatientWeight
816: (0010,21B0) LT #0 [] AdditionalPatientHistory
824: (0018,0020) CS #2 [SE] ScanningSequence
834: (0018,0021) CS #2 [SK] SequenceVariant
844: (0018,0022) CS #2 [SP] ScanOptions
854: (0018,0023) CS #2 [2D] MRAcquisitionType
864: (0018,0024) SH #4 [fse] SequenceName
876: (0018,0050) DS #8 [5.00000] SliceThickness
892: (0018,0080) DS #8 [2300.00] RepetitionTime
908: (0018,0081) DS #8 [22.0000] EchoTime
924: (0018,0083) DS #8 [1.00000] NumberOfAverages
940: (0018,0084) DS #8 [63.8615] ImagingFrequency
956: (0018,0086) IS #2 [1] EchoNumbers
966: (0018,0087) DS #8 [1.50000] MagneticFieldStrength
982: (0018,0088) DS #8 [2.00000] SpacingBetweenSlices
998: (0018,0089) IS #4 [256] NumberOfPhaseEncodingSteps
1010: (0018,0091) IS #2 [8] EchoTrainLength
1020: (0018,0095) DS #8 [31.2500] PixelBandwidth
1036: (0018,1020) LO #2 [3] SoftwareVersions
1046: (0018,1030) LO #14 [CLINICAL BRAIN] ProtocolName
1068: (0018,1088) IS #2 [0] HeartRate
1078: (0018,1090) IS #2 [0] CardiacNumberOfImages
1088: (0018,1094) IS #2 [0] TriggerWindow
1098: (0018,1100) DS #8 [220.000] ReconstructionDiameter
1114: (0018,1250) SH #4 [HEAD] ReceiveCoilName
1126: (0018,1310) US #8 [0\256\256\0] AcquisitionMatrix
1142: (0018,1312) CS #4 [ROW] InPlanePhaseEncodingDirection
1154: (0018,1314) DS #2 [90] FlipAngle
1164: (0018,1316) DS #10 [0.0313309] SAR
1182: (0018,5100) CS #4 [HFS] PatientPosition
1194: (0020,000D) UI #36 [0.0.0.0.2.8811.20010413115754.12432] StudyInstanceUI
1238: (0020,000E) UI #38 [0.0.0.0.3.8811.2.20010413115754.12432] SeriesInstanc
1284: (0020,0010) SH #4 [8811] StudyID
1296: (0020,0011) IS #2 [2] SeriesNumber
1306: (0020,0012) IS #6 [31762] AcquisitionNumber
1320: (0020,0013) IS #2 [19] InstanceNumber
1330: (0020,0020) CS #4 [L\PH] PatientOrientation
1342: (0020,0030) DS #26 [-110.500\-95.2063\52.1425] ImagePosition
1376: (0020,0032) DS #26 [-110.500\-95.2063\52.1425] ImagePositionPatient
1410: (0020,0035) DS #50 [1.00000\0.00000\0.00000\0.00000\0.990960\0.134158] I
1468: (0020,0037) DS #50 [1.00000\0.00000\0.00000\0.00000\0.990960\0.134158] I
1526: (0020,0052) UI #38 [0.0.0.0.4.8811.2.20010413115754.12432] FrameOfRefere
1572: (0020,1002) IS #2 [1] ImagesInAcquisition
1582: (0020,1040) LO #2 [NA] PositionReferenceIndicator
1592: (0020,1041) DS #8 [66.9000] SliceLocation
1608: (0028,0002) US #2 [1] SamplesPerPixel
1618: (0028,0004) CS #12 [MONOCHROME2] PhotometricInterpretation
1638: (0028,0010) US #2 [256] Rows
1648: (0028,0011) US #2 [256] Columns
1658: (0028,0030) DS #18 [0.859375\0.859375] PixelSpacing
1684: (0028,0100) US #2 [16] BitsAllocated
1694: (0028,0101) US #2 [16] BitsStored
1704: (0028,0102) US #2 [15] HighBit
1714: (0028,0103) US #2 [1] PixelRepresentation
1724: (0028,0106) SS #2 [0] SmallestImagePixelValue
1734: (0028,0107) SS #2 [860] LargestImagePixelValue
1744: (0028,0120) SS #2 [0] PixelPaddingValue
1754: (0028,1050) DS #2 [0] WindowCenter
1764: (0028,1051) DS #2 [0] WindowWidth
1774: (0028,1052) DS #2 [0] RescaleIntercept
1784: (0028,1053) DS #2 [1] RescaleSlope
1794: (0028,1054) LO #28 [SIGNAL INTENSITY (UNITLESS)] RescaleType
1830: (7FE0,0010) OW #131072 [0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\

C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>
```



<a name="ZLDBT"></a>
# [dcmldap](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-dcmldap/README.md): Insert/remove configuration entries for Network AEs into/from LDAP server
在LDAP服务器中插入/删除网络AEs的配置项

作用<br />向LDAP服务器添加删除服务

<a name="n5Y70"></a>
## 示例1 向LDAP服务器添加storescp服务
```
$ dcmldap -c STORESCP@storescp:11112
create new Device 'storescp' providing Network AE 'STORESCP' on Network Connection 'dicom' listing on host 'storescp' at port '11112', using Distinguished Name 'cn=admin,dc=dcm4che,dc=org' and password 'secret' to bind to LDAP server 'ldap://localhost:389/dc=dcm4che,dc=org'.
```



<a name="ETe6W"></a>
# [dcmqrscp](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-dcmqrscp/README.md): Simple DICOM archive

<a name="vr3hY"></a>
## 示例1 本地开启storescp服务
```
Example: dcmqrscp -b DCMQRSCP:11112 --dicomdir /media/cdrom/DICOMDIR

$ dcmqrscp -b DCMQRSCP:11112 --dicomdir series-000001/DICOMDIR
dcmqrscp -b DCMQRSCP:11112 --dicomdir disk99/DICOMDIR
dcmqrscp -b DCMQRSCP:11112 --dicomdir resources/DICOMDIR

//启动服务对于查询只有这一个是成功的
dcmqrscp -b DCMQRSCP:11112 --dicomdir .\DICOMDIR

=> Starts server listening on port 11112, accepting association requests with DCMQRSCP as called AE title.
```




<a name="RO3t7"></a>
# [dcmvalidate](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-dcmvalidate/README.md): Validate DICOM object according a specified Information Object Definition

<a name="XgU5b"></a>
## 示例1 验证DICOM对象，通过IOD
```
$ dcmvalidate --iod etc/dcmvalidate/dicomdir-iod.xml DICOMDIR
Validate DICOMDIR against IOD specified in etc/dcmvalidate/dicomdir.xml
```




<a name="m4aUZ"></a>
# [deidentify](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-deidentify/README.md): De-identify one or several DICOM files
根据DICOM第15部分中规定的基本应用级保密配置文件，取消识别一个或多个DICOM文件。

<a name="MVsrh"></a>
## 示例1 修改DICOM文件的相关字段
```
Examples:
$ deidentify --retain-uid -s PatientName=ANONYMIZED -s PatientID=0815 MR01.dcm out.dcm

这样是可以使用的
deidentify MR01.dcm out.dcm

这样是不可以使用的，说MR01.dcmXXX的问题【java.lang.IllegalArgumentException: MR01.dcm】
deidentify -s PatientName=ANONYMIZED MR01.dcm out.dcm

这样是可以的【添加了--】
deidentify -s PatientName=ANONYMIZED -- MR01.dcm out.dcm

1.113654.5.15.1504.dcm
CT.dcm
CT_60594d0b57e84e01.dcm
des1.2.840.10008.1.2.4.70.DCM
image.dcm
image2.dcm


$ deidentify -sStudyInstanceUID=1.2.3.4 image.dcm out.dcm
deidentify --retain-uid -sStudyInstanceUID=1.2.3.4 image.dcm out.dcm

De-identify DICOM file in.dcm to out.dcm, retaining UIDs in the Attributes, setting Patient Name und Patient ID to the specified values.
```
将DICOM文件in.dcm取消标识为out.dcm，在属性中保留UID，将患者姓名和患者ID设置为指定值。


<a name="Evoik"></a>
# [emf2sf](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-emf2sf/README.md): Convert DICOM Enhanced Multi-frame image to legacy DICOM Single-frame images

<a name="1s8Ss"></a>
## 示例1 提取出多帧DICOM为单帧DICOM
```
$ emf2sf -f 1,20,120 --out-file ct-000.dcm ct-emf.dcm
Extract frame 1, 20 and 120 from Enhanced CT Multi-frame image ct-emf.dcm to legacy DICOM Single-frame CT images ct-001.dcm, ct-020.dcm and
ct-120.dcm.
```

<a name="8mlw1"></a>
# [findscu](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-findscu/README.md): Invoke DICOM C-FIND Query Request


<a name="5xpku"></a>
## 示例1 查询
```
Example 1: findscu -c DCMQRSCP@localhost:11112 -m PatientName=Doe^John -m StudyDate=20110510- -m ModalitiesInStudy=CT

findscu -c DCMQRSCP@localhost:11112 -m PatientID=123565

findscu -c DCMQRSCP@localhost:11112 -m StudyInstanceUID=1.113654.3.13.1026


1.113654.3.13.1026

=> Query Query/Retrieve Service Class Provider DCMQRSCP listening on local port 11112 for CT Studies for Patient John Doe since 2011-05-10
```


<a name="AFxvx"></a>
## 示例2 查询.xml文件定义的相关字段
```
Example 2: findscu -c DCMQRSCP@localhost:11112 -- /etc/findscu/study.xml

findscu -c DCMQRSCP@localhost:11112 -- .\mwl.xml
findscu -c DCMQRSCP@10.128.217.225:11113 -- .\mwl.xml

C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\etc\findscu\study.xml

=> Query Query/Retrieve Service Class Provider DCMQRSCP listening on local port 11112 with query keys provided in /etc/findscu/study.xml file
```



<a name="Tx0ks"></a>
## 示例3 关联查询/级联查询
```
Example 3: findscu -c DCMQRSCP@localhost:11112 --relational -L IMAGE -m Modality=PR -x ~/dcm4che/etc/findscu/pr.csv.xsl --out-cat --out-file test.csv --out-dir ~/work/ -- ~/dcm4che/etc/findscu/pr.xml
=> Perform relational query on Image level on Query/Retrieve Service Class Provider DCMQRSCP listening on local port 11112 by applying the presentation state stylesheet with query keys provided in /etc/findscu/pr.xml file and Modality as PR; concatenate the results to a csv file in a specific directory.
```



<a name="lbDas"></a>
# [getscu](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-getscu/README.md): Invoke DICOM C-GET Retrieve Request
![image.png](https://cdn.nlark.com/yuque/0/2021/png/2869223/1628499970562-52cbf74f-b3d4-4e7d-b1d4-e6648d16f0a0.png#height=290&id=ep0DN&originHeight=579&originWidth=1103&originalType=binary&ratio=1&rotation=0&showTitle=false&size=120511&status=done&style=none&title=&width=551.5)
<a name="ktnuU"></a>
## 示例1 获取服务端的DICOM文件
失败<br />两个问题【这两个问题，应该是版本问题，5.23.2版本有问题，5.21.0版本应该没问题】<br />1 如果UID太长就不通过，比如2.25.58330658964102801470888741497237625832<br />2 如果UID长度合适，也会出现IO异常，如下图

```
$ .\getscu --connect DCMQRSCP@localhost:11112 -m PatientID=123565

//默认请求级别的STUDY
getscu -c DCMQRSCP@localhost:11112 -m StudyInstanceUID=1.113654.3.13.1026


getscu -L PATIENT -c DCMQRSCP@localhost:11112 -m PatientID=123565

getscu -L SERIES -c DCMQRSCP@localhost:11112 -m PatientID=123565

1.113654.3.13.1026


1.2.392.200046.100.2.1.47095101944.150601080213
1.113654.3.13.1026
0.0.0.0.2.8811.20010413115754.12432
2.25.58330658964102801470888741497237625832

getscu -c DCMQRSCP@localhost:11112 -m StudyInstanceUID=2.25.58330658964102801470888741497237625832
getscu -c DCMQRSCP@localhost:11112 -m StudyInstanceUID=1.113654.3.13.1026
.\getscu -c DCMQRSCP@localhost:11112 -m StudyInstanceUID=1.2.392.200046.100.2.1.47095101944.150601080213

0.0.0.0.2.8811.20010413115754.12432
.\getscu --connect DCMQRSCP@localhost:11112 --directory store -m PatientID=123565



 -c,--connect <aet@host:port>             specify AE Title, remote address
                                          and port of the remote
                                          Application Entity.
    --cancel-after <ms>                   cancel retrieve after specified
                                          time in ms.
    --connect-timeout <ms>                timeout in ms for TCP connect,
                                          no timeout by default
    --directory <path>                    directory to which retrieved
                                          DICOM Composite Objects are
                                          stored, '.' by default

store
Retrieve from Query/Retrieve Service Class Provider DCMQRSCP listening on local port 11112 the Study with Study Instance UID = 1.2.3.4, negotiating Storage Transfer Capabilities defined by etc/getscu/store-tcs.properties.
Retrieved objects are stored to the working directory.
```

```
12:34:38,665 DEBUG - DCMQRSCP<-GETSCU(2) >> 1:C-GET-RQ Dataset receiving...
12:34:38,665 WARN  - IOException during read of (FFFE,E000) #-1 @ 8
java.io.IOException: internal error: length should have been validated in readHeader
        at org.dcm4che3.io.DicomInputStream.readValue(DicomInputStream.java:849)
        at org.dcm4che3.io.DicomInputStream.readValue(DicomInputStream.java:699)
        at org.dcm4che3.io.DicomInputStream.readFragments(DicomInputStream.java:834)
        at org.dcm4che3.io.DicomInputStream.readValue(DicomInputStream.java:597)
        at org.dcm4che3.io.DicomInputStream.readAttributes(DicomInputStream.java:576)
        at org.dcm4che3.io.DicomInputStream.readDataset(DicomInputStream.java:503)
        at org.dcm4che3.io.DicomInputStream.readDataset(DicomInputStream.java:487)
        at org.dcm4che3.net.PDUDecoder.readDataset(PDUDecoder.java:507)
        at org.dcm4che3.net.service.AbstractDicomService.readDataset(AbstractDicomService.java:82)
        at org.dcm4che3.net.service.AbstractDicomService.onDimseRQ(AbstractDicomService.java:74)
        at org.dcm4che3.net.service.DicomServiceRegistry.onDimseRQ(DicomServiceRegistry.java:86)
        at org.dcm4che3.net.ApplicationEntity.onDimseRQ(ApplicationEntity.java:474)
        at org.dcm4che3.net.Association.onDimseRQ(Association.java:755)
        at org.dcm4che3.net.PDUDecoder.decodeDIMSE(PDUDecoder.java:466)
        at org.dcm4che3.net.Association.handlePDataTF(Association.java:738)
        at org.dcm4che3.net.State$4.onPDataTF(State.java:103)
        at org.dcm4che3.net.Association.onPDataTF(Association.java:734)
        at org.dcm4che3.net.PDUDecoder.nextPDU(PDUDecoder.java:177)
        at org.dcm4che3.net.Association$2.run(Association.java:571)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)
12:34:38,665 INFO  - DCMQRSCP<-GETSCU(2): i/o exception: java.io.IOException: internal error: length should have been validated in readHeader in State: Sta6 - Association established and ready for data transfer
12:34:38,681 INFO  - DCMQRSCP<-GETSCU(2): close Socket[addr=/127.0.0.1,port=9883,localport=11112]
12:34:38,681 DEBUG - DCMQRSCP<-GETSCU(2): enter state: Sta1 - Idle
```

<a name="BXpi7"></a>
## 过程中的问题
没有表示上下文，有些DICOM文件可以传输有些不可以，应该是传输语法的问题<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/2869223/1628505512190-5bb68c29-ba46-47ea-a542-d567c5e55681.png#height=521&id=FgGQw&originHeight=1042&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=211045&status=done&style=none&title=&width=960)

**什么的表示上下文？**<br />表示上下文包括传输语法和抽象语法<br />SOP Class UID（Service Object Pair Class Unique Identifier）标识客户端需要的服务<br />在连接上下文中，被发送的SOP Class 也被叫做抽象语义Abstract Syntax<br />因此Abstract Syntax就是SOP Class UID的同义词<br />。在传输SOP Class UID（即Abstract Syntax）的同时，会发送与该服务对应的编码格式，即Transfer Syntaxes。<br />以乳腺检查的X光片为例，通常乳腺X光片很大，需要进行压缩。客户端在向服务端发送上下文信息时会提供给服务端一种乳腺X光片的压缩方式，例如JPEG2000，同时也会提供一种被大多数图像传输服务端接受的非压缩方式。如下图所示：<br />![](https://cdn.nlark.com/yuque/0/2021/png/2869223/1628505473471-25d6a3cf-614e-46d4-a7f4-641037d7f2bc.png#height=422&id=vam1U&originHeight=422&originWidth=958&originalType=binary&ratio=1&rotation=0&showTitle=false&size=0&status=done&style=none&title=&width=958)



**什么是传输语法？**<br />就是大端小端，显示，隐式<br />传输时候使用<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/2869223/1628504827602-051eb1b3-627c-4a6f-bfd1-31d2c2616736.png#height=57&id=yY6fm&originHeight=113&originWidth=1640&originalType=binary&ratio=1&rotation=0&showTitle=false&size=25203&status=done&style=none&title=&width=820)


**什么的抽象语法？**


![image.png](https://cdn.nlark.com/yuque/0/2021/png/2869223/1628504549175-a3a8fc77-2de1-400e-b90d-a28f53a22f55.png#height=521&id=nGnDL&originHeight=1042&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=211045&status=done&style=none&title=&width=960)
<a name="FfZfZ"></a>
# [hl72xml](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-hl72xml/README.md): Convert HL7 v2.x message in XML presentation

<a name="LbIR8"></a>
## 示例1 hl7转化成xml格式
```
hl7文件位置
C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\etc\testdata\hl7
文件名
ADT^A08.hl7
OMI^O23.hl7
ORM^O01.hl7



Examples:
$ hl72xml C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\etc\testdata\hl7\message.hl7
Write XML representation of HL7 file message.hl7 to standard output

```

<a name="1IJft"></a>
## 结果
```
C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>hl72xml C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\etc\testdata\hl7\message.hl7

原始内容
MSH|^~\&|MESA_OF|XYZ_RADIOLOGY|MESA_IM|XYZ_IMAGE_MANAGER|||ADT^A08|100212|P|2.5.1|||||| ||
EVN||200007011800||||200007011800
PID||1234^^^CHIP-ISSUER|583295^^^ADT1|1234^^^TATTOO-ISSUER|Smith^Shadow||20141014|F^|Smith^Mark||||||||||||||||||||||||||388490000^Canis^SCT|25243005^Toy poodle dog breed^SCT||
PV1||E|ED||||1234^WEAVER^TIMOTHY^P^^DR|5101^NELL^FREDERICK^P^^DR||HSR|||||AS||0000^Admitting^Doctor^P^^DR||V100^^^ADT1|||||||||||||||||||||||||200008201100|||||||V|

转换后的内容
<?xml version="1.0" encoding="UTF-8"?>
<hl7 xml-space="preserved">
    <MSH fieldDelimiter="|" componentDelimiter="^" repeatDelimiter="~" escapeDelimiter="\" subcomponentDelimiter="&amp;">
        <field>MESA_OF</field>
        <field>XYZ_RADIOLOGY</field>
        <field>MESA_IM</field>
        <field>XYZ_IMAGE_MANAGER</field>
        <field/>
        <field/>
        <field>ADT<component>A08</component>
        </field>
        <field>100212</field>
        <field>P</field>
        <field>2.5.1</field>
        <field/>
        <field/>
        <field/>
        <field/>
        <field/>
        <field></field>
        <field/>
        <field/>
    </MSH>
    <EVN>
        <field/>
        <field>200007011800</field>
        <field/>
        <field/>
        <field/>
        <field>200007011800</field>
    </EVN>
    <PID>
        <field/>
            <field>1234
            <component/>
            <component/>
            <component>CHIP-ISSUER</component>
            </field>
            <field>583295<component/>
            <component/>
            <component>ADT1</component>
        </field>
        <field>1234
            <component/>
            <component/>
            <component>TATTOO-ISSUER</component>
        </field>
        <field>Smith
            <component>Shadow</component>
        </field>
        <field/>
            <field>20141014</field>
            <field>F
                <component/>
            </field>
        <field>Smith
            <component>Mark</component>
        </field>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field>388490000<component>Canis</component>
<component>SCT</component>
</field>
<field>25243005<component>Toy poodle dog breed</component>
<component>SCT</component>
</field>
<field/>
<field/>
</PID>
<PV1>
<field/>
<field>E</field>
<field>ED</field>
<field/>
<field/>
<field/>
<field>1234<component>WEAVER</component>
<component>TIMOTHY</component>
<component>P</component>
<component/>
<component>DR</component>
</field>
<field>5101<component>NELL</component>
<component>FREDERICK</component>
<component>P</component>
<component/>
<component>DR</component>
</field>
<field/>
<field>HSR</field>
<field/>
<field/>
<field/>
<field/>
<field>AS</field>
<field/>
<field>0000<component>Admitting</component>
<component>Doctor</component>
<component>P</component>
<component/>
<component>DR</component>
</field>
<field/>
<field>V100<component/>
<component/>
<component>ADT1</component>
</field>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field>200008201100</field>
<field/>
<field/>
<field/>
<field/>
<field/>
<field/>
<field>V</field>
<field/>
</PV1>
</hl7>
C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>
```



<a name="7ekpt"></a>
# [hl7pdq](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-hl7pdq/README.md): Query HL7 v2.x Patient Demographics Supplier
查询HL7 v2.x患者人口统计信息供应商

hl7pdq应用程序使用HL7 V2.5 QBP^Q22消息，根据IHE ITI-21事务：患者人口统计查询，向患者人口统计供应商查询与指定人口统计字段匹配的患者人口统计信息、所有或指定域列表。

```
The hl7pdq application queries a Patient Demographics Supplier for patient demographic information matching specified Demographics Fields, for all or for a specified list of domains using a HL7 V2.5 QBP^Q22 message according IHE ITI-21 Transaction: Patient Demographics Query.
```
 HL7 V2.5 QBP^Q22 message<br />IHE ITI-21 Transaction: Patient Demographics Query.


<a name="yjezO"></a>
## 示例1
```
Examples:
hl7pdq -c XREF^XYZ@localhost:2575 @PID.3.1^XYZ10515W @PID.3.4.1^XREF2005
=> Query Patient Demographics Supplier XREF^XYZ listening on local port 2575 for a patient with Patient ID XYZ10515W with Assigning Authority Namespace ID XREF2005
```




<a name="LIHjf"></a>
# [hl7pix](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-hl7pix/README.md): Query HL7 v2.x PIX Manager
查询HL7 v2.x PIX管理器<br />hl7pix应用程序根据ITI-9事务：PIX查询，使用HL7 V2.5 QBP^Q23消息，向PIX管理器查询与指定患者ID对应的所有或指定域列表的患者ID。

```
Example: hl7pix -c XREF^XYZ@localhost:2575 XYZ10515W^^^XREF2005
=> Query PIX Manager XREF^XYZ listening on local port 2575 for Patient IDs
for all domains that correspond Patient ID XYZ10515W with Assigning
Authority Namespace ID XREF2005
```

<a name="uhNpC"></a>
# [hl7rcv](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-hl7rcv/README.md): HL7 v2.x Receiver
```
The hl7rcv application receives HL7 V2 messages from a HL7 Sender application using Minimal Lower Level Protocol (MLLP).
```
hl7rcv应用程序使用最小低级协议（MLLP）从HL7发送方应用程序接收HL7 V2消息。

<a name="CqsUS"></a>
## 示例1 接收hl7文件
```
Example: hl7rcv -b 2575
=> Starts receiver listening on port 2575.
```

<a name="8kjbE"></a>
## 结果
```

C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>hl7rcv -b 2575
13:11:21,668 INFO  - Start TCP Listener on 0.0.0.0/0.0.0.0:2575
13:12:25,428 INFO  - Accept connection Socket[addr=/127.0.0.1,port=4455,localport=2575]
13:12:25,428 INFO  - Socket[addr=/127.0.0.1,port=4455,localport=2575] >> MSH|^~\&|MESA_OF|XYZ_RADIOLOGY|MESA_IM|XYZ_IMAGE_MANAGER|||ADT^A08|100212|P|2.5.1|||||| ||
13:12:25,443 DEBUG - Socket[addr=/127.0.0.1,port=4455,localport=2575] >> MSH|^~\&|MESA_OF|XYZ_RADIOLOGY|MESA_IM|XYZ_IMAGE_MANAGER|||ADT^A08|100212|P|2.5.1|||||| ||
EVN||200007011800||||200007011800
PID||1234^^^CHIP-ISSUER|583295^^^ADT1|1234^^^TATTOO-ISSUER|Smith^Shadow||20141014|F^|Smith^Mark||||||||||||||||||||||||||388490000^Canis^SCT|25243005^Toy poodle dog breed^SCT||
PV1||E|ED||||1234^WEAVER^TIMOTHY^P^^DR|5101^NELL^FREDERICK^P^^DR||HSR|||||AS||0000^Admitting^Doctor^P^^DR||V100^^^ADT1|||||||||||||||||||||||||200008201100|||||||V|

13:12:25,443 INFO  - M-WRITE .\ADT^A08\100212
13:12:25,443 INFO  - Socket[addr=/127.0.0.1,port=4455,localport=2575] << MSH|^~\&|MESA_IM|XYZ_IMAGE_MANAGER|MESA_OF|XYZ_RADIOLOGY|20210809131225.443||ACK^A08^ACK|1107190340|P|2.5.1|||||| ||
13:12:25,443 DEBUG - Socket[addr=/127.0.0.1,port=4455,localport=2575] << MSH|^~\&|MESA_IM|XYZ_IMAGE_MANAGER|MESA_OF|XYZ_RADIOLOGY|20210809131225.443||ACK^A08^ACK|1107190340|P|2.5.1|||||| ||
MSA|AA|100212|

13:12:25,443 INFO  - Close connection Socket[addr=/127.0.0.1,port=4455,localport=2575]

```

<a name="s0Zox"></a>
# [hl7snd](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-hl7snd/README.md): Send HL7 v2.x message
```
Reads HL7 V2 messages from specified files or directories and send them to a HL7 Receiver application listening on specified host and port using Minimal Lower Level Protocol (MLLP). Specify '-' as <file> to read the message from standard input
```

<a name="OGsSH"></a>
## 示例1 发送hl7文件
```
Example: hl7snd -c localhost:2575 message.hl7
hl7snd -c 1.116.91.38:2576 message.hl7

hl7snd -c localhost:2575 C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\etc\testdata\hl7\message.hl7

hl7snd -c localhost:2575 message.hl7

=> Send HL7 V2 message message.hl7 to HL7 Receiver listening on local port 2575.

```
<a name="oXhuF"></a>
## 结果
```

C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>hl7snd -c localhost:2575 C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\etc\testdata\hl7\message.hl7
13:12:25,417 INFO  - Initiate connection from 0.0.0.0/0.0.0.0:0 to localhost:2575
13:12:25,428 INFO  - Established connection Socket[addr=localhost/127.0.0.1,port=2575,localport=4455]
13:12:25,428 INFO  - Socket[addr=localhost/127.0.0.1,port=2575,localport=4455] << MSH|^~\&|MESA_OF|XYZ_RADIOLOGY|MESA_IM|XYZ_IMAGE_MANAGER|||ADT^A08|100212|P|2.5.1|||||| ||
13:12:25,428 DEBUG - Socket[addr=localhost/127.0.0.1,port=2575,localport=4455] << MSH|^~\&|MESA_OF|XYZ_RADIOLOGY|MESA_IM|XYZ_IMAGE_MANAGER|||ADT^A08|100212|P|2.5.1|||||| ||
EVN||200007011800||||200007011800
PID||1234^^^CHIP-ISSUER|583295^^^ADT1|1234^^^TATTOO-ISSUER|Smith^Shadow||20141014|F^|Smith^Mark||||||||||||||||||||||||||388490000^Canis^SCT|25243005^Toy poodle dog breed^SCT||
PV1||E|ED||||1234^WEAVER^TIMOTHY^P^^DR|5101^NELL^FREDERICK^P^^DR||HSR|||||AS||0000^Admitting^Doctor^P^^DR||V100^^^ADT1|||||||||||||||||||||||||200008201100|||||||V|

13:12:25,443 INFO  - Socket[addr=localhost/127.0.0.1,port=2575,localport=4455] >> MSH|^~\&|MESA_IM|XYZ_IMAGE_MANAGER|MESA_OF|XYZ_RADIOLOGY|20210809131225.443||ACK^A08^ACK|1107190340|P|2.5.1|||||| ||
13:12:25,443 DEBUG - Socket[addr=localhost/127.0.0.1,port=2575,localport=4455] >> MSH|^~\&|MESA_IM|XYZ_IMAGE_MANAGER|MESA_OF|XYZ_RADIOLOGY|20210809131225.443||ACK^A08^ACK|1107190340|P|2.5.1|||||| ||
MSA|AA|100212|

13:12:25,443 INFO  - Close connection Socket[addr=localhost/127.0.0.1,port=2575,localport=4455]

C:\Users\admin\Desktop\dcm4che-5.23.2-bin\dcm4che-5.23.2\bin>
```
<a name="bHFLR"></a>
# [ianscp](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-ianscp/README.md): DICOM Instance Availability Notification receiver
DICOM实例可用性通知接收器

```
The ianscp application implements a Service Class Provider (SCP) for the Instance Available Notification (IAN) SOP Class. It listens on a specific TCP/IP port for incoming association requests from a Service Class User (SCU) of the IAN SOP Class. Instance Available Notifications received in N-CREATE requests are stored in DICOM files. ianscp application also supports the Verification Service Class as a SCP.

ianscp应用程序为实例可用通知（IAN）SOP类实现服务类提供程序（SCP）。它在特定的TCP/IP端口上侦听来自IAN SOP类的服务类用户（SCU）的传入关联请求。N-CREATE请求中接收的实例可用通知存储在DICOM文件中。ianscp应用程序还支持验证服务类作为SCP。
```



<a name="XkKjM"></a>
## 示例1
```
Example: ianscp -b IANSCP:11112
=> Starts server listening on port 11112, accepting association requests with IANSCP as called AE title Instance Available Notifications will be stored in the working directory.
```


<a name="oljFJ"></a>
# [ianscu](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-ianscu/README.md): Send DICOM Instance Availability Notification

```
The ianscu application implements a Service Class User (SCU) for the Instance Available Notification (IAN) SOP Class and for the Verification SOP Class.
DICOM files specified on the command line may contain IAN or Composite Objects. Files with filename extension '.xml' are parsed as XML Infoset of the native DICOM Model specified in DICOM Part 19.
Specified IAN objects are sent verbatim by one IAN N-CREATE to a Service Class Provider (SCP) of the IAN SOP Class. 
For Composite Objects for each different Study an IAN N-CREATE referencing the SOP Instances in the scanned files is sent to a Service Class Provider (SCP) of the IAN SOP Class.
If no DICOM file is specified, it sends a DICOM C-ECHO message and waits for a response.
```

<a name="MIR4Q"></a>
## 示例1
使用的N-CREATE命令
```
Example: ianscu -c IANSCP@localhost:11112 --retrieve-aet QRSCP path/to/study

ianscu -c IANSCP@localhost:11112 --retrieve-aet QRSCP images/DICOM

 --retrieve-aet <aet>                  specify value for Retrieve AE Title (0008,0054), AE Title of this application by default

=> Scan images in directory path/to/study and send an IAN N-CREATE RQ referencing the images as ONLINE retrievable from QRSCP, to IANSCP listening on local port 11112.
```



<a name="A7NNn"></a>
# [jpg2dcm](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-jpg2dcm/README.md): Convert JPEG images or MPEG videos in DICOM files



<a name="bh1jN"></a>
## 示例1 把.jpg图片和xml文件头信息生成.dcm图像
```
Example 1: jpg2dcm -f metadata.xml image.jpg image.dcm
=> Encapsulate JPEG Image verbatim with DICOM attributes specified in
metadata.xml into DICOM Image Object.
```

<a name="mSNup"></a>
## 示例2 直接把jpg图片转换成.dcm图像
```
Example 2: jpg2dcm --no-app -m PatientName=Simson^Homer -m PatientSex=M -- homer.jpg image.dcm
=> Encapsulate JPEG Image without application segments with specified
DICOM attributes into DICOM Image Object.
```


<a name="bVZO3"></a>
## 示例3 把.mpeg视频转换成dcm图像
MPEG标准主要有以下五个，MPEG-1、MPEG-2、MPEG-4、MPEG-7及MPEG-21等。
```
Example 3: jpg2dcm video.mpeg video.dcm
=> Encapsulate MPEG2 Video into DICOM Video Object.
```

<a name="Ht0ng"></a>
## 示例4 把mp4视频转换成dcm图像

<a name="I5qqR"></a>
## 示例5 .jpeg图片和.mp4视频分别转化成dcm文件

注意，新版本的才有，旧版本的不存在
```
Example 5: jpg2dcm img1.jpeg video1.mp4 dicom-dir
=> Encapsulate the specified image and video files to DICOM objects in
dicom-dir.
```

![image.png](https://cdn.nlark.com/yuque/0/2021/png/2869223/1628558926912-22621ad1-3e3c-4c36-8078-56075fe8567d.png#height=290&id=mdCxg&originHeight=579&originWidth=1103&originalType=binary&ratio=1&rotation=0&showTitle=false&size=30706&status=done&style=none&title=&width=551.5)<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/2869223/1628558937633-25c0b682-69a7-4eb2-8556-f37f5a4fdc30.png#height=521&id=mluz2&originHeight=1042&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=194540&status=done&style=none&title=&width=960)

<a name="uCY7G"></a>
# [json2props](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-json2props/README.md): Convert Archive configuration schema JSON files to key/value properties files and vice versa
将归档配置模式JSON文件转换为键/值属性文件，反之亦然
```
The json2props utility converts Archive configuration schema JSON files to key-value properties files and vice versa to ease translation of attribute names and descriptions to other languages than English.
```


<a name="OxuQs"></a>
# [json2rst](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-json2rst/README.md): Generate ReStructuredText files from Archive configuration schema JSON files
从存档配置架构JSON文件生成重构的文本文件
```
the json2rst utility generates ReStructuredText files from Archive configuration schema JSON files used for documentation of Archive configuration attributes in the DICOM Conformance Statement.

json2rst实用程序从用于记录DICOM一致性语句中的归档配置属性的归档配置模式JSON文件生成重构的DTEXT文件。
```

<a name="W84Op"></a>
# [mkkos](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-mkkos/README.md): Make DICOM Key Object Selection Document
制作DICOM键对象选择文档

Create DICOM Key Object Selection Document (KOS) with specified Document Title -title <code> flagging DICOM Composite objects in specified <file>.. and <directory>.. and store it into DICOM file -o <file>.<br />创建具有指定文档标题的DICOM关键对象选择文档（KOS）-标题<code>在指定的<file>中标记DICOM复合对象。。和<directory>。。并将其存储到DICOM文件-o<file>中。

```
Example: mkkos --title DCM-113000 -o kos.dcm path/to/study
=> Create DICOM Key Object Selection Document with Document Title (113000, DCM, "Of Interest") flagging Composite objects in directory path/to/study and store it into DICOM file kos.dcm.
```


<a name="JnfnM"></a>
# [modality](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-ihe/dcm4che-tool-ihe-modality/README.md): Simulates DICOM Modality

模拟DICOM模态

The modality application provides a configurable work-flow tool integrating Key Objects, MPPS, C-STORE, and Storage Commitment. The tool can:<br />模态应用程序提供了一个可配置的工作流工具，集成了关键对象、MPPS、C-STORE和存储承诺。该工具可以：

* Create a DICOM Key Object Selection Document (KOS) with specified Document Title -title <code> flagging DICOM Composite objects in specified <file>.. and <directory>.. and store it into DICOM file -o <file>.<br />可以实现mkkos工具功能<br />*创建具有指定文档标题的DICOM关键对象选择文档（KOS）-标题<code>标记指定<file>中的DICOM复合对象。。和<directory>。。并将其存储到DICOM文件-o<file>中。

* Send a MPPS N-CREATE and MPPS N-SET message referencing the SOP Instances in the scanned files to a Service Class Provider (SCP) of the MPPS SOP Class.<br />*将引用扫描文件中SOP实例的MPPS N-CREATE和MPPS N-SET消息发送给MPPS SOP类的服务类提供商（SCP）。

* Send a C-STORE message to a Storage Service Class Provider (SCP) for each DICOM file on the command line and wait for a response.<br />*在命令行上为每个DICOM文件向存储服务类提供程序（SCP）发送一条C-STORE消息，并等待响应。

* Send a Storage Commitment Requests (N-ACTION RQ) for the SOP Instances in the scanned files to a Service Class Provider (SCP) of the Storage Commitment Push Model SOP Class.<br />*将扫描文件中SOP实例的存储承诺请求（N-ACTION RQ）发送给存储承诺推送模型SOP类的服务类提供商（SCP）。
<a name="89LA0"></a>
## 示例1 发送DICOM图像到DCM4CHEE，发送DICOM图像存储承诺到DCM4CHEE
关于存储承诺，存储一批DICOM图像，只需要发送一个存储承诺到存储端，命令是N-ACTION-RQ，SCP返回来的命令是N-ACTION-RSP


Example: modality --stgcmt -b MODALITY:11114 -c DCM4CHEE@localhost:11112 path/to/study

modality --stgcmt -b MODALITY:11114 -c DCM4CHEE@192.168.175.128:11112 path/to/study

=> i) Start server listening on port 11114, accepting association requests with MODALITY as called AE title (for receiving Storage Commitment Results). <br />启动服务器监听端口 11114，接受 MODALITY 的关联请求，称为 AE 标题（用于接收存储承诺结果）。<br />ii) Scan images in directory  path/to/study and send a MPPS N-CREATE RQ and MPPS N-SET RQ referencing the images to DCM4CHEE, listening on local port 11112 (to send MPPS N-SET RQ after sending the DICOM objects use option --mpps-late). <br />扫描目录路径/to/study中的图像，并将引用图像的MPPS N-CREATE RQ和MPPS N-SET RQ发送到DCM4CHEE，在本地端口11112上侦听（要在发送DICOM对象后发送MPPS N-SET RQ，请使用选项--MPPS late）<br />iii) Send DICOM objects to DCM4CHEE@localhost:11112. <br />将DICOM对象发送到DCM4CHEE@localhost:11112.<br />iv) Send a Storage Commitment Request for SOP Instances in directory path/to/study to DCM4CHEE@localhost:11112.<br />发送路径/to/study to目录中SOP实例的存储承诺请求DCM4CHEE@localhost:11112.

<a name="WFhR6"></a>
## 结果

<a name="oTPeM"></a>
# [movescu](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-movescu/README.md): Invoke DICOM C-MOVE Retrieve request
调用DICOM C-MOVE检索请求

The movescu application implements a Service Class User (SCU) for the Query/Retrieve, the Composite Instance Root Retrieve, the Composite Instance Retrieve Without Bulk Data, the Hanging Protocol Query/Retrieve and the Color Palette Query/Retrieve Service Class. <br />movescu应用程序为查询/检索、复合实例根检索、无批量数据的复合实例检索、挂起协议查询/检索和调色板查询/检索服务类实现服务类用户（SCU）。<br />movescu only supports retrieve functionality using the C-MOVE message.<br />movescu仅支持使用C-MOVE消息检索功能。<br />It sends matching keys to an Service Class Provider (SCP) and waits for responses. Matching keys can be specified in DICOM file(s) -<dicom-file>... or by options -m.<br />它将匹配的密钥发送到服务类提供程序（SCP）并等待响应。可以在DICOM文件--<DICOM文件>中指定匹配的密钥。。。或者通过选项-m。

<a name="S5QVV"></a>
## 示例1 把图像从DCMQRSCP移动到STORESCP
Examples: $ movescu -c DCMQRSCP@localhost:11112 -m StudyInstanceUID=1.113654.3.13.1026 --dest STORESCP

Retrieve from Query/Retrieve Service Class Provider DCMQRSCP listening on local port 11112 the Study with Study Instance UID = 1.2.3.4 to the Storage Service Class Provider STORESCP<br />从查询/检索服务类提供程序DCMQRSCP检索本地端口11112上侦听的研究实例UID=1.2.3.4的研究到存储服务类提供程序STORESCP
<a name="hZ4Yi"></a>
# [mppsscp](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-mppsscp/README.md): DICOM Modality Performed Procedure Step Receiver
DICOM模态执行程序步进接收机<br />The mppsscp application implements a Service Class Provider (SCP) for the **Modality Performed Procedure Step (MPPS) SOP Class**. <br />mppsscp应用程序为模态执行过程步骤（MPPS）SOP类实现服务类提供程序（SCP）。<br />It listens on a specific TCP/IP port for incoming association requests from a Service Class User (SCU) of the MPPS SOP Class.<br />它在特定的TCP/IP端口上侦听来自MPPS SOP类的服务类用户（SCU）的传入关联请求。<br /> MPPS objects received in **N-CREATE** requests are stored in DICOM files which are updated on receive of **N-SET** requests for that MPPS instance. <br />在N-CREATE请求中接收的MPPS对象存储在DICOM文件中，该文件在收到该MPPS实例的N-SET请求时更新。<br />mppsscp application also supports the Verification Service Class as a SCP.<br />mppsscp应用程序还支持**验证服务类**作为SCP

<a name="4brRX"></a>
## 示例1
Example: mppsscp -b MPPSSCP:11112

=> Starts server listening on port 11112, accepting association requests with MPPSSCP as called AE title. Received Modality Performed Procedure Step objects will be stored in the working directory.
<a name="R2DHH"></a>
# [mppsscu](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-mppsscu/README.md): Send DICOM Modality Performed Procedure Step
The mppsscu application implements a Service Class User (SCU) for the Modality Performed Procedure Step (MPPS) SOP Class** and** for the Verification SOP Class. <br />mppsscu应用程序为模态执行程序步骤（MPPS）SOP类和验证SOP类实现服务类用户（SCU）。<br />DICOM files specified on the command line may contain MPPS or Composite Objects. <br />命令行上指定的DICOM文件可能包含MPP或复合对象。<br />Files with filename extension '.xml' are parsed as XML Infoset of the native **DICOM Model** specified in DICOM Part 19. <br />文件扩展名为“.xml”的文件被解析为DICOM第19部分中指定的本机DICOM模型的xml信息集。<br />Specified MPPS objects are sent verbatim by one MPPS N-CREATE and one MPPS N-SET message to a Service Class Provider (SCP) of the MPPS SOP Class. <br />指定的MPPS对象通过一条MPPS N-CREATE和一条MPPS N-SET消息逐字发送到MPPS SOP类的服务类提供程序（SCP）。<br />For Composite Objects for each different Study a MPPS N-CREATE and a MPPS N-SET message referencing the SOP Instances is sent to the MPPS SCP. <br />对于每个不同研究的复合对象，将参考SOP实例的MPPS N-CREATE和MPPS N-SET消息发送至MPPS SCP。<br />If no DICOM file is specified, it sends a DICOM C-ECHO message and waits for a response.<br />如果未指定DICOM文件，则会发送DICOM C-ECHO消息并等待响应。
<a name="IBh3A"></a>
## 示例1
Example: mppsscu -c MPPSSCP@localhost:11112 path/to/study

=> Scan images in directory  path/to/study and send a MPPS N-CREATE RQ and a MPPS N-SET RQ referencing the images to MPPSSCP, listening on local port 11112.

<a name="xB7sC"></a>
# [pdf2dcm](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-pdf2dcm/README.md): Convert PDF file into DICOM file
Encapsulate bulkdata PDF (extension pdf), CDA (extension xml), STL (extension stl), MTL (extension mtl) or OBJ (extension obj) file(s) (or present in directories) into DICOM file(s) (or into DICOM directory). <br />将bulkdata PDF（扩展名PDF）、CDA（扩展名xml）、STL（扩展名STL）、MTL（扩展名MTL）或OBJ（扩展名OBJ）文件（或存在于目录中）封装到DICOM文件（或DICOM目录中）。<br />DICOM attributes can be specified via command line (using -m option) or a XML file (using -f option).<br />可以通过命令行（使用-m选项）或XML文件（使用-f选项）指定DICOM属性。<br /> If no option is specified, by default they are generated using sample metadata file present in the tool. <br />如果未指定任何选项，默认情况下，将使用工具中的示例元数据文件生成这些选项。<br />If all the options are specified, system will generate metadata first from sample metadata file then from file specified by user and lastly add the attributes specified individually on command line. <br />如果指定了所有选项，系统将首先从示例元数据文件生成元数据，然后从用户指定的文件生成元数据，最后在命令行上添加单独指定的属性。<br />The Type 1 and Type 2 attributes, if missing, will be generated by the system.<br />类型1和类型2属性（如果缺失）将由系统生成。


<a name="PAzNt"></a>
## 示例1
Example 1: pdf2dcm -f metadata.xml file.pdf object.dcm

pdf2dcm -f encapsulatedCDAMetadata.xml file.pdf object.dcm

=> Encapsulate PDF document with DICOM attributes specified in metadata.xml into DICOM Object.

<a name="FNcJj"></a>
# [stgcmtscu](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-stgcmtscu/README.md): Invoke DICOM Storage Commitment Request
是发送还是接收？<br />The stgcmtscu application implements a Service Class User (SCU) for the Storage Commitment Push Model SOP Class. DICOM files specified on the command line are scanned and one or several Storage Commitment Requests (N-ACTION RQ) for the SOP Instances in the scanned files are sent to a Service Class Provider (SCP) of the Storage Commitment Push Model SOP Class. If no DICOM file is specified, it sends a DICOM C-ECHO message and waits for a response.

<a name="L4zVo"></a>
## 示例1 发送存储承诺请求，接收存储承诺结果
会查询当前的DICOM文件在SCP存储端的存储承诺<br />会接受到一个文件，很小，应该是存储承诺文件，内容是什么？<br />Example: stgcmtscu -b STGCMTSCU:11114 -c DCMQRSCP@localhost:11112 path/to/study

stgcmtscu -b STGCMTSCU:11114 -c DCM4CHEE@192.168.175.128:11112 path/to/study

=> Starts server listening on port 11114, accepting association requests with STGCMTSCU as called AE title, for receiving Storage Commitment Results, before sending a Storage Commitment Request for SOP Instances in directory path/to/study to Storage Service Class Provider DCMQRSCP, listening on local port 11112.
<a name="DBdIY"></a>
# [storescp](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-storescp/README.md): DICOM Composite Object Receiver
DICOM复合目标接收机<br />The storescp application implements a Service Class Provider (SCP) for the Storage Service Class.<br />storescp应用程序为存储服务类实现服务类提供程序（SCP）。<br /> It listens on a specific TCP/IP port for incoming association requests from a Storage Service Class User (SCU) and can receive DICOM images and other DICOM Composite Objects. <br />它在特定的TCP/IP端口上侦听来自存储服务类用户（SCU）的传入关联请求，并可以接收DICOM映像和其他DICOM复合对象。<br />The storescp application also supports the Verification Service Class as a SCP.<br />storescp应用程序还支持验证服务类作为SCP。


<a name="OdmS1"></a>
## 示例1 接收DICOM图像
Example: storescp -b STORESCP:11112

=> Starts server listening on port 11112, accepting association requests with STORESCP as called AE title. Received objects are stored to the working directory.<br />启动服务器侦听端口11112，接受与STORESCP的关联请求（称为AE title）。接收到的对象存储到工作目录中。
<a name="V70j9"></a>
# [storescu](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-storescu/README.md): Send DICOM Composite Objects
The storescu application implements a Service Class User (SCU) for the** Storage Service Class **and for the **Verification SOP Class**. <br />storescu应用程序为存储服务类和验证SOP类实现服务类用户（SCU）。<br />For each DICOM file on the command line it sends a C-STORE message to a Storage Service Class Provider (SCP) and waits for a response. <br />对于命令行上的每个DICOM文件，它向存储服务类提供程序（SCP）发送一条C-STORE消息，并等待响应。<br />If no DICOM file is specified, it sends a DICOM C-ECHO message and waits for a response. <br />如果未指定DICOM文件，则会发送DICOM C-ECHO消息并等待响应。<br />The application can be used to transmit DICOM images and other DICOM composite objects and to verify basic DICOM connectivity.<br />该应用程序可用于传输DICOM图像和其他DICOM复合对象，并验证基本DICOM连接。
<a name="gmfC9"></a>
## 示例1 发送DICOM图像
Example: storescu -c STORESCP@localhost:11112 series-000001\image-000002.dcm


=> Send DICOM image image.dcm to Storage Service Class Provider STORESCP, listening on local port 11112.




<a name="MJLkX"></a>
# [stowrs](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-stowrs/README.md): Send DICOM Composite Objects or Bulkdata file over Web
For DICOM files : Send multiple dicom files or directories containing DICOM files to STOW-RS receiver at a time. <br />For metadata+bulkdata : Send multiple bulkdata files or directories containing bulkdata files to STOW-RS receiver at a time. <br />DICOM attributes can be specified via command line (using -m option) or a XML file (using -f option) <br />If both the options are specified, system will generate metadata first from sample metadata file then from file specified by user and lastly add the attributes specified individually on command line.<br /> The Type 1 and Type 2 attributes, if missing, will be generated by the system. <br />Storing bulkdata files of various Content types in one request is not possible ，i.e for eg. one can send multiple pdfs in one request, but can not send combination of files like pdfs and images in one request. 不可能在一个请求中存储各种内容类型的海量数据文件，例如，一个请求可以发送多个PDF，但不能在一个请求中发送PDF和图像等文件的组合。<br />Supported content types for bulkdata are application/pdf, text/xml (for CDA files), image/jpeg, video/mpeg, video/mp4, video/quicktime, image/jp2, image/png, image/gif, application/sla (for STL files), model/mtl (for MTL files) and model/obj (for OBJ files). <br />For metadata : Send single non bulkdata type of file by specifying a metadata file for objects like Structured Reports, Presentation States etc. File names shall not contain spaces.

Options:<br /> -a,--accept <arg>                Specify the value for Accept header : xml or json. <br />The value of Accept header will then be sent in request header as application/dicom+xml or application/dicom+json. <br />For DICOM objects, if this option is not set then application/dicom+xml will be used always by default.<br />If this flag is absent, for bulkdata type of objects the value specified in -t option will be used to determine application/dicom+xml or application/dicom+json. <br />If -t option is absent as well then application/dicom+xml will be used by default.

   --allowAnyHost                If the other server requires HTTPS and this option is specified, the other server’s certificate is validated via  the truststore, but host name validation is not done.如果另一台服务器需要HTTPS并且指定了此选项，则会通过信任库验证另一台服务器的证书，但不会进行主机名验证




<a name="qXet8"></a>
## 示例1
Example: stowrs -m PatientName=John^Doe --url http[s]://<host>:<port>/dcm4chee-arc/aets/{AETitle}/rs/studies img.jpeg

[http://192.168.180.128:8080/weasis-pacs-connector/weasis?studyUID=1.113654.3.13.1026](http://192.168.180.128:8080/weasis-pacs-connector/weasis?studyUID=1.113654.3.13.1026)

stowrs -m PatientName=test --url [http://localhost:8080/stowrs/studies](http://localhost:8080/stowrs/studies/) img1.jpeg

【下面这些指令都是可以的】<br />对于URL连接没有什么要求<br />并且会把多个dcm图像放入一个文件里，和dcm文件格式基本相同，不过会添加分界标识<br />**stowrs -m PatientName=test --url **[**http://localhost:8080/stowrsd/studies**](http://localhost:8080/stowrs/studies/)** MR01.dcm MR02.dcm**<br />**stowrs -m PatientName=test --url **[**http://localhost:8080/stowrs/studies**](http://localhost:8080/stowrs/studies/)** MR01.dcm**<br />**stowrs -m PatientName=test --url http://localhost:8080/dcm4chee-arc/studies MR01.dcm**<br />**stowrs -m PatientName=test --url **[**http://localhost:8080**](http://localhost:8080)**/dcm4chee-arc/aets/AETitle/rs/studies MR01.dcm**<br />**stowrs -m PatientName=test --url http://localhost:8080 MR01.dcm**


=> Send stow request to stowRS Receiver with the attribute given and img.jpeg bulkData.

<a name="lajBt"></a>
# [stowrsd](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-stowrsd/README.md): STOW-RS Server
STOW-RS，DICOM存储服务

这个5.21.0版本没有，5.23.2版本有

Store over the Web using RESTful Services

The stowrsd application implements a STOW-RS server. Objects in received multipart requests may be unpacked and stored in individual files.<br />stowrsd应用程序实现一个STOW-RS服务器。接收到的多部分请求中的对象可以解包并存储在单个文件中。

Options:<br /> -b,--bind <[ip:]port>  <br />specify the port on which the STOW-RS Server shall listening for connection requests. <br />If no local IP address of the network interface is specified, connections on any/all local addresses are accepted.

   --backlog <no>       maximum number of queued incoming connections.  Use system default if not specified.

 -d,--directory <path>   <br />directory under which received data is stored.<br />With --unpack, extracted objects are stored in sub-directories. <br />Sub-directory / file names encodes the date-time of the request. <br />'.' by default.

 -h,--help               <br />display this help and exit

    --ignore             do not store received data in files.

 -t,--threads <no>       maximum number of concurrently handled requests, 1 by default.

 -u,--unpack             unpack objects from received multipart requests.从收到的多部分请求中解压缩对象。

-V,--version            output version information and exit
<a name="hSM9N"></a>
## 示例1
Example: stowrsd -b 8080

=> Starts server listening on port 8080.
<a name="q5mfJ"></a>
# swappxdata: Swaps bytes of uncompressed pixel data in DICOM files
交换DICOM文件中未压缩像素数据的字节<br />The swappxdata utility swaps bytes of uncompressed pixel data with Value Representation OW. For each successfully updated file a dot (.) character is written to stdout. For each file kept untouched, one of the characters:<br />p - no pixel data)<br />c - compressed pixel data)<br />b - pixel data with Value Representation OB)<br />l - little endian encoded pixel data)<br />8 - pixel data with 8 bits allocated)<br />is written to stdout. <br />If an error occurs on updating a file, an E character is written to stdout and a stack trace is written to stderr.如果在更新文件时发生错误，则会将E字符写入标准输出，并将堆栈跟踪写入标准输出。


<a name="wzpBY"></a>
## 示例1
Example: swappxdata --if-big-endian --log '{0020000D};{0020000E};{00080018}' storage/2020/09/

=> Swaps bytes of big endian encoded pixel data in DICOM files in directory storage/2020/09/ and logs the Study Instance UID (0020,000D}, Series Instance UID (0020,000E) and SOP Instance UID (0008,0018) of updated objects separated by semicolons to ./uids.log.<br />在目录storage/2020/09/中交换DICOM文件中的big-endian编码像素数据字节，并将更新对象的研究实例UID（002000D}、系列实例UID（002000E）和SOP实例UID（00080018）记录到./uids.log。
<a name="v2mFA"></a>
# [syslog](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-syslog/README.md): Send Syslog messages via TCP/TLS or UDP to a Syslog Receiver




<a name="0nyK7"></a>
# [syslogd](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-syslogd/README.md): Receives RFC 5424 Syslog messages via TCP/TLS or UDP




<a name="63PhO"></a>
# [upsscu](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-upsscu/README.md): Invokes services of Unified Procedure Step Service Class

<a name="1CCGL"></a>
# [wadors](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-wadors/README.md): Wado RS Client Simulator
WADO-RS，DICOM获取服务

Web Access to DICOM Persistent Objects using RESTful Services

Wado客户机模拟器

Wado RS client simulator. It supports retrieving Study, Series, Instance, Metadata and Bulkdata. One may choose to specify multiple urls as arguments. Each of the objects of the Study/series shall be saved to the current working directory or the directory selected by user as <uid>-001.dicom, <uid>-002.dicom and so on. The uid is determined based on the url(s) specified. For eg. if study is retrieved the Study IUID will be used, if the url is for series retrieval then Series IUID shall be used. The extension of individual parts is determined by content type of each part.
<a name="XK5Tg"></a>
## 示例1 通过WADO RS服务从服务器获得DICOM图像
Examples: => wadors http[s]://<host>:<port>/dcm4chee-arc/aets/{AETitle}/rs/studies/{StudyIUID1 }<br />Send WADO RS request to Wado RS Receiver to retrieve studies with Study Instance UID StudyIUID1

wadors **https**://192.168.175.128:8080/dcm4chee-arc/aets/DCM4CHEE/rs/studies/1.113654.3.13.1026<br />【这个是可以的，上边的不可以，SSL异常，不支持或无法识别的SSL消息，http和https的区别】<br />**wadors **[**http://192.168.175.128:8080/dcm4chee-arc/aets/DCM4CHEE/rs/studies/1.113654.3.13.1026**](https://192.168.175.128:8080/dcm4chee-arc/aets/DCM4CHEE/rs/studies/1.113654.3.13.1026)<br />**【不行，AET还是需要指定的】**<br />wadors [http://192.168.175.128:8080/dcm4chee-arc/aets/test/rs/studies/1.113654.3.13.1026](http://192.168.175.128:8080/dcm4chee-arc/aets/DCM4CHEE/rs/studies/1.113654.3.13.1026)<br />【不行，必须要指定为dcm4chee-arc】<br />wadors [http://192.168.175.128:8080/dcm4chee/aets/DCM4CHEE/rs/studies/1.113654.3.13.1026](http://192.168.175.128:8080/dcm4chee-arc/aets/DCM4CHEE/rs/studies/1.113654.3.13.1026)<br />【也不可以，这里的每一个字段都是特定的，不能任意更改的】<br />wadors [http://192.168.175.128:8080/dcm4chee-arc/aets/DCM4CHEE/rs/studie/1.113654.3.13.1026](http://192.168.175.128:8080/dcm4chee-arc/aets/DCM4CHEE/rs/studies/1.113654.3.13.1026)
<a name="oAF6e"></a>
# [wadows](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-wadows/README.md): Wado WS Client Simulator
<a name="IYbsW"></a>
## 示例1 
Example: wadows --url http://<host>:<port>/dcm4chee-arc/xdsi/ImagingDocumentSource --rendered --study '1.113654.3.13.1026[1.113654.5.14.1035[1.113654.5.15.1504,1.113654.5.15.1512]]'<br />=> Send RetrieveRenderedImagingDocumentSetRequest to Wado WS Receiver with specified StudyIUID[SeriesIUID[SOPIUID1,SOPIUID2]] with default content type as image/jpeg.

wadows --url http://192.168.175.128:8080/dcm4chee-arc/xdsi/ImagingDocumentSource --rendered --study '0.0.0.0.2.8811.20010413115754.12432[0.0.0.0.3.8811.2.20010413115754.12432[0.0.0.0.1.8811.2.11.20010413115754.12432]]'

wadows --url [http://192.168.175.128:8080/dcm4chee-arc/xdsi/ImagingDocumentSource](http://192.168.175.128:8080/dcm4chee-arc/xdsi/ImagingDocumentSource) --rendered --study '1.113654.3.13.1026[1.113654.5.14.1035[1.113654.5.15.1504]]'
```

C:\Users\admin\Desktop\dcm4che-5.21.0-bin\dcm4che-5.21.0\bin>wadows --url http://192.168.175.128:8080/dcm4chee-arc/xdsi/ImagingDocumentSource --rendered --study '1.113654.3.13.1026[1.113654.5.14.1035[1.113654.5.15.1504,1.113654.5.15.1512]]'
17:26:29,988 INFO  - >> RetrieveRenderedImagingDocumentSetRequest:
17:26:29,989 INFO  -   Study[uid='1.113654.3.13.1026]
17:26:29,989 INFO  -    Series[uid=1.113654.5.14.1035]
17:26:29,989 INFO  -     Document[uid=1.113654.5.15.150]
17:26:29,989 INFO  -     Repository[uid=1.3.6.1.4.1.21367.13.80.110]
17:26:31,138 INFO  - << RetrieveRenderedImagingDocumentSetResponse:

C:\Users\admin\Desktop\dcm4che-5.21.0-bin\dcm4che-5.21.0\bin>
```

<a name="pTZRs"></a>
## 示例2
=> Send RetrieveImagingDocumentSet to Wado WS Receiver with default Transfer Syntax UID as Explicit VR Little Endian and Key Object Selection Document having Current Requested Procedure Evidence Sequence which contains StudyIUID, SeriesIUID(s) and DocumentUniqueID(s).<br />将RetrieveImagingDocumentSet发送到Wado WS-Receiver，默认传输语法UID为显式VR Little-Endian和关键对象选择文档，该文档具有当前请求的过程证据序列，其中包含StudyUID、SerieUID和DocumentUniqueID。

<a name="Gq0Eh"></a>
# [xml2dcm](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-xml2dcm/README.md): Create/Update DICOM file from/with XML presentation



<a name="DV612"></a>
# [xml2hl7](https://github.com/dcm4che/dcm4che/blob/master/dcm4che-tool/dcm4che-tool-xml2hl7/README.md): Create HL7 v2.x message from XML presentation




