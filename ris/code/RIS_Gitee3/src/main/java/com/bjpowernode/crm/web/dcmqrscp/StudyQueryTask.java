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
import org.dcm4che3.net.Association;
import org.dcm4che3.net.Status;
import org.dcm4che3.net.pdu.PresentationContext;
import org.dcm4che3.net.service.DicomServiceException;
import org.dcm4che3.util.StringUtils;

import java.io.IOException;

class StudyQueryTask extends PatientQueryTask {
    private WorkListDao workListDao = SqlSessionUtil.getSqlSession().getMapper(WorkListDao.class);
    protected final String[] studyIUIDs;
    protected Attributes studyRec;
    protected int studyRecNum = 0;
    /**
     *
     * @param as
     * @param pc
     * @param rq
     * @param keys 要查的字段，包括完整字段和空字段，完整字段是查询条件，空字段是要查询的
     * @param qrscp
     * @throws DicomServiceException
     */
    public StudyQueryTask(Association as, PresentationContext pc, Attributes rq, Attributes keys, DcmQRSCP qrscp)
            throws DicomServiceException {
        //内部完成了查询，完成的是PATIENT层级查询
        super(as, pc, rq, keys, qrscp);
        studyIUIDs = StringUtils.maskNull(keys.getStrings(Tag.StudyInstanceUID));//提取出非空的字段作为查询条件
        //内部完成了查询，完成的是STUDY层级查询
        wrappedFindNextStudy();
    }

    @Override
    public boolean hasMoreMatches() throws DicomServiceException {
        return studyRec != null;
    }

    @Override
    public Attributes nextMatch() throws DicomServiceException {
        Attributes ret = new Attributes(patRec.size() + studyRec.size());
        ret.addAll(patRec);
        ret.addAll(studyRec, true);
        wrappedFindNextStudy();
        return ret;
    }

    private void wrappedFindNextStudy() throws DicomServiceException {
        try {
            findNextStudy();
        } catch (IOException e) {
            throw new DicomServiceException(Status.UnableToProcess, e);
        }
    }

    protected boolean findNextStudy() throws IOException {
        /*
        先看有没有这个病人，然后看有没有这个病人的检查
         */
//        System.out.println("-------查询第一个study-----------");
        if (patRec == null) {
            return false;
        }
        /*
         第一次进行查询时候，studyRec为空，同patRec第一次查询也是空
         */
        if (studyRec == null) {

            studyRecNum = SqlUtil.workListCache.size();
            System.out.println("查询到个数："+SqlUtil.workListCache.size());
            studyRec = new Attributes();

            WorkList workList = SqlUtil.workListCache.get(studyRecNum-1);
            studyRec.setString(Tag.AccessionNumber, VR.SH, workList.getAccessionNumber());
            studyRec.setString(Tag.Modality, VR.SH, workList.getModality());
            studyRec.setString(Tag.StudyInstanceUID, VR.SH, workList.getStudyInstanceUID());
            studyRec.setString(Tag.RequestedProcedureDescription, VR.SH, workList.getRequestedProcedureDescription());
            studyRec.setString(Tag.ScheduledStationAETitle, VR.SH, workList.getScheduledStationAETitle());
            studyRec.setString(Tag.ScheduledProcedureStepStartDate, VR.SH,  workList.getScheduledProcedureStepStartDate());
            studyRec.setString(Tag.ScheduledProcedureStepStartTime, VR.SH,  workList.getScheduledProcedureStepStartTime());
            studyRec.setString(Tag.ScheduledProcedureStepDescription, VR.SH,  workList.getScheduledProcedureStepDescription());
            studyRec.setString(Tag.ScheduledProcedureStepID, VR.SH, workList.getScheduledProcedureStepID());
            studyRec.setString(Tag.RequestedProcedureID, VR.SH, workList.getRequestedProcedureID());
            studyRec.setString(Tag.SpecificCharacterSet, VR.CS, "GB18030");
            studyRec.setString(Tag.SpecificCharacterSet, VR.PN, "GB18030");

            studyRecNum=studyRecNum-1;
        } else  if(studyRecNum==0){
            //第一次之后
            //studyRecNum==0 往后没有了
            studyRec=null;
        }else{
            //第一次之后，往后还有
            studyRec = new Attributes();
            WorkList workList = SqlUtil.workListCache.get(studyRecNum-1);
            studyRec.setString(Tag.AccessionNumber, VR.SH, workList.getAccessionNumber());
            studyRec.setString(Tag.Modality, VR.SH, workList.getModality());
            studyRec.setString(Tag.StudyInstanceUID, VR.SH, workList.getStudyInstanceUID());
            studyRec.setString(Tag.RequestedProcedureDescription, VR.SH, workList.getRequestedProcedureDescription());
            studyRec.setString(Tag.ScheduledStationAETitle, VR.SH, workList.getScheduledStationAETitle());
            studyRec.setString(Tag.ScheduledProcedureStepStartDate, VR.SH,  workList.getScheduledProcedureStepStartDate());
            studyRec.setString(Tag.ScheduledProcedureStepStartTime, VR.SH,  workList.getScheduledProcedureStepStartTime());
            studyRec.setString(Tag.ScheduledProcedureStepDescription, VR.SH,  workList.getScheduledProcedureStepDescription());
            studyRec.setString(Tag.ScheduledProcedureStepID, VR.SH, workList.getScheduledProcedureStepID());
            studyRec.setString(Tag.RequestedProcedureID, VR.SH, workList.getRequestedProcedureID());
            studyRec.setString(Tag.SpecificCharacterSet, VR.CS, "GB18030");
            studyRec.setString(Tag.SpecificCharacterSet, VR.PN, "GB18030");

            studyRecNum=studyRecNum-1;

        }

        while (studyRec == null && super.findNextPatient())
            studyRec = ddr.findStudyRecord(patRec, keys, recFact, ignoreCaseOfPN, matchNoValue);



        return studyRec != null;
    }
}