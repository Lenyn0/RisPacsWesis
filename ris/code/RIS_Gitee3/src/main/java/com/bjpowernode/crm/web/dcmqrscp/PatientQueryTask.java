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
 * Portions created by the Initial Developer are Copyright (C) 2011
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

package com.bjpowernode.crm.web.dcmqrscp;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.web.dao.WorkListDao;
import com.bjpowernode.crm.web.domain.WorkList;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.media.DicomDirReader;
import org.dcm4che3.media.RecordFactory;
import org.dcm4che3.net.Association;
import org.dcm4che3.net.Status;
import org.dcm4che3.net.pdu.PresentationContext;
import org.dcm4che3.net.service.BasicQueryTask;
import org.dcm4che3.net.service.DicomServiceException;
import org.dcm4che3.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PatientQueryTask extends BasicQueryTask {
    private WorkListDao workListDao = SqlSessionUtil.getSqlSession().getMapper(WorkListDao.class);
    protected final String[] patIDs;
    protected final DicomDirReader ddr;
    protected final RecordFactory recFact;
    protected final String availability;
    protected final boolean ignoreCaseOfPN;
    protected final boolean matchNoValue;
    protected final int delayCFind;
    protected Attributes patRec;

    public PatientQueryTask(Association as, PresentationContext pc, Attributes rq, Attributes keys, DcmQRSCP qrscp)
            throws DicomServiceException {
        //把 keys 保存进基类 BasicQueryTask 中，应该是用来进行查询操作的
        super(as, pc, rq, keys);
        this.patIDs = StringUtils.maskNull(keys.getStrings(Tag.PatientID));
        this.ddr = qrscp.getDicomDirReader();
        this.recFact = qrscp.getRecordFactory();
        this.availability = qrscp.getInstanceAvailability();
        this.ignoreCaseOfPN = qrscp.isIgnoreCaseOfPN();
        this.matchNoValue = qrscp.isMatchNoValue();
        this.delayCFind = qrscp.getDelayCFind();
        //内部完成了查询
        wrappedFindNextPatient();
    }

    @Override
    public boolean hasMoreMatches() throws DicomServiceException {
        return patRec != null;
    }

    @Override
    public Attributes nextMatch() throws DicomServiceException {
        Attributes tmp = patRec;
        wrappedFindNextPatient();
        return tmp;
    }

    @Override
    protected Attributes adjust(Attributes match) {
        Attributes adjust = super.adjust(match);
        adjust.remove(Tag.DirectoryRecordType);
        if (keys.contains(Tag.SOPClassUID))
             adjust.setString(Tag.SOPClassUID, VR.UI,
                     match.getString(Tag.ReferencedSOPClassUIDInFile));
        if (keys.contains(Tag.SOPInstanceUID))
             adjust.setString(Tag.SOPInstanceUID, VR.UI,
                     match.getString(Tag.ReferencedSOPInstanceUIDInFile));
        adjust.setString(Tag.QueryRetrieveLevel, VR.CS,
                keys.getString(Tag.QueryRetrieveLevel));
        adjust.setString(Tag.RetrieveAETitle, VR.AE, as.getCalledAET());
        if (availability != null)
            adjust.setString(Tag.InstanceAvailability, VR.CS, availability);
//        adjust.setString(Tag.StorageMediaFileSetID, VR.SH, ddr.getFileSetID());
//        adjust.setString(Tag.StorageMediaFileSetUID, VR.UI, ddr.getFileSetUID());
        match.setString(Tag.SOPClassUID, VR.UI,
                match.getString(Tag.ReferencedSOPClassUIDInFile));
        match.setString(Tag.SOPInstanceUID, VR.UI,
                match.getString(Tag.ReferencedSOPInstanceUIDInFile));
        if (delayCFind > 0)
            try {
                Thread.sleep(delayCFind);
            } catch (InterruptedException ignore) {}
        return adjust;
    }

    private void wrappedFindNextPatient() throws DicomServiceException {
        try {
            findNextPatient();
        } catch (IOException e) {
            throw new DicomServiceException(Status.UnableToProcess, e);
        }
    }

    /**
     *
     * @return 有没有查询成功
     * @throws IOException
     */
    protected boolean findNextPatient() throws IOException {
        /*
        初始时候patRec为空，所以会进入，之后就不为空了，就会进入后边的地方
         */
//        System.out.println("--------查询了第一个patient-------------");
        //第一次查询时候
        if (patRec == null) {
            // 内部完成了查询 这里完成了查询，patRec接收查询结果


            Map<String, Object> myMap=new HashMap<>();
            myMap.put("patientID",keys.getString(Tag.PatientID));
            SqlUtil.workListCache = workListDao.getWorkList(myMap);
            if (SqlUtil.workListCache.size() == 0) {
                return false;
            }
            WorkList workList =SqlUtil.workListCache.get(0);
            System.out.println("查询到个数："+SqlUtil.workListCache.size());
            patRec=new Attributes();

            patRec.setString(Tag.PatientID, VR.SH, workList.getPatientID());
            patRec.setString(Tag.PatientName, VR.SH, workList.getPatientName());
            patRec.setString(Tag.PatientBirthDate, VR.SH, workList.getPatientBirthDate());
            patRec.setString(Tag.PatientSex, VR.SH, workList.getPatientSex());



        } else {
            //第一次之后

            patRec = null;


        }
//            if (patIDs.length == 1)
//            patRec = null;
//        else
//            patRec = ddr.findNextPatientRecord(patRec, keys, recFact, ignoreCaseOfPN, matchNoValue);
        //返回的结果是有没有查询成功
        return patRec != null;
    }
}