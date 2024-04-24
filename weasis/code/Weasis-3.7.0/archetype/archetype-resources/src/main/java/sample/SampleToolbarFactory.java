package sample; /*******************************************************************************
 * Copyright (c) 2009-2020 Weasis Team and other contributors.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

//#set( $symbol_pound = '#' )
//#set( $symbol_dollar = '$' )
//#set( $symbol_escape = '\' )
//package ${package};

import java.util.Hashtable;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.weasis.core.api.media.data.ImageElement;
import org.weasis.core.api.gui.Insertable;
import org.weasis.core.api.gui.Insertable.Type;
import org.weasis.core.api.gui.InsertableFactory;
import org.weasis.core.ui.editor.SeriesViewerFactory;
import org.weasis.core.ui.editor.image.ImageViewerPlugin;
import org.weasis.core.ui.editor.image.ViewCanvas;
import org.weasis.dicom.codec.DicomImageElement;
import org.weasis.dicom.explorer.CheckTreeModel;
import org.weasis.dicom.explorer.DicomModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.weasis.dicom.explorer.DicomExportFactory;
import java.util.Properties;
import org.weasis.dicom.explorer.ExportDicom;
import org.weasis.dicom.viewer2d.EventManager;
import org.weasis.dicom.viewer2d.View2dContainer;

@org.osgi.service.component.annotations.Component(service = InsertableFactory.class, immediate = false, property = {
"org.weasis.dicom.viewer2d.View2dContainer=true"  })

public class SampleToolbarFactory implements InsertableFactory {
    private final Logger LOGGER = LoggerFactory.getLogger(SampleToolbarFactory.class);
    @org.osgi.service.component.annotations.Reference private DicomModel model;
    @org.osgi.service.component.annotations.Reference private SeriesViewerFactory seriesViewerFactory;

    @Override
    public Type getType() {
        return Type.TOOLBAR;
    }

    @Override
    public Insertable createInstance(Hashtable<String, Object> properties) {
//        return new SampleToolBar<ImageElement>();

       return new SampleToolBar();


    }

    @Override
    public boolean isComponentCreatedByThisFactory(Insertable component) {
        return component instanceof SampleToolBar;
    }

    @Override
    public void dispose(Insertable bar) {
        if (bar != null) {
            // Remove all the registered listeners or other behaviors links with other existing components if exists.
        }
    }

    @Activate
    protected void activate(ComponentContext context) throws Exception {
        LOGGER.info("Activate the Sample tool bar");
    }


    @Deactivate
    protected void deactivate(ComponentContext context) {
        LOGGER.info("Deactivate the Sample tool bar");
    }

}
/*@org.osgi.service.component.annotations.Component(
        service = DicomExportFactory.class,
        immediate = false)
public class SampleToolbarFactory implements DicomExportFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleToolbarFactory.class);

    public static final Properties EXPORT_PERSISTENCE = new Properties();

    @Override
    public ExportDicom createDicomExportPage(Hashtable<String, Object> properties) {
        if (properties != null) {
            DicomModel dicomModel = (DicomModel) properties.get(DicomModel.class.getName());
            CheckTreeModel treeModel = (CheckTreeModel) properties.get(CheckTreeModel.class.getName());
            if (dicomModel != null && treeModel != null) {
                //return new IsoImageExport(dicomModel, treeModel);
                SampleToolBar temp=new SampleToolBar(properties);
                return null;
                //return new SampleToolBar(properties);
            }
        }
        return null;
    }

    @Activate
    protected void activate(ComponentContext context) throws Exception {
        LOGGER.info("Export ISO image is activated");
        FileUtil.readProperties(
                new File(BundlePreferences.getDataFolder(context.getBundleContext()), "export.properties"),
                EXPORT_PERSISTENCE); //$NON-NLS-1$
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        LOGGER.info("Export ISO image is deactivated");
        FileUtil.storeProperties(
                new File(BundlePreferences.getDataFolder(context.getBundleContext()), "export.properties"),
                EXPORT_PERSISTENCE,
                null); //$NON-NLS-1$
    }
}*/
