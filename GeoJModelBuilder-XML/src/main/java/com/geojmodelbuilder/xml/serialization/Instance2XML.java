package com.geojmodelbuilder.xml.serialization;

import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import jdk.internal.org.xml.sax.InputSource;
import net.opengis.ows.x20.CodeType;
import net.opengis.ows.x20.LanguageStringType;
import net.opengis.wps.x20.DataDocument;
import net.opengis.wps.x20.DataDocument.Data;
import net.opengis.wps.x20.LiteralDataType.LiteralDataDomain;
import net.opengis.wps.x20.DataInputType;
import net.opengis.wps.x20.LiteralDataDocument;
import net.opengis.wps.x20.LiteralDataType;
import net.opengis.wps.x20.ReferenceType;
import cn.edu.whu.geos.wls.x10.ProcessInstanceType;
import cn.edu.whu.geos.wls.x10.ProcessInstanceType.ExecType;
import cn.edu.whu.geos.wls.x10.WPSEnvDocument;
import cn.edu.whu.geos.wls.x10.WPSEnvDocument.WPSEnv;
import cn.edu.whu.geos.wls.x10.WPSEnvDocument.WPSEnv.Mode;
import cn.edu.whu.geos.wls.x10.WorkflowInstanceDocument;
import cn.edu.whu.geos.wls.x10.WorkflowInstanceType;

public class Instance2XML {
	public static void main(String[] args)
	{
	
		WorkflowInstanceDocument document = WorkflowInstanceDocument.Factory.newInstance();
		
		WorkflowInstanceType workflowInstance = document.addNewWorkflowInstance();
		
		//set the title
		LanguageStringType titleType = workflowInstance.insertNewTitle(0);
		titleType.setStringValue("Water Extraction Workflow");
		
		//set the abstract
		LanguageStringType abstractType = workflowInstance.insertNewAbstract(0);
		abstractType.setStringValue("water extraction from remote sensing images");
		
		//set the identifier
		CodeType identifier = workflowInstance.addNewIdentifier();
		identifier.setStringValue("http://geos.whu.edu.cn/workflow/instance/water_extraction");
		
		// process
		ProcessInstanceType process1 = workflowInstance.insertNewProcessInstance(0);
		process1.setExecType(ExecType.Enum.forString("OGC_WPS"));
		
		String process1_id = "http://geos.whu.edu.cn/wps10/RasterMapcalcProcess/843f0661-3661-11e7-b6bc-3417ebce41fe";
		CodeType process1_idty =  process1.addNewIdentifier();
		process1_idty.setStringValue(process1_id);
		
		process1.setName("RasterMapcalcProcess");
		
		
        //ExecEnvType envType = process1.addNewExecEnv();
        WPSEnvDocument wpsEnvDoc = WPSEnvDocument.Factory.newInstance();
		//WPSEnvDocumentImpl wpsEnvDoc = new  WPSEnvDocumentImpl(null);
		//wpsEnvDoc.setUrl("");
//		wpsEnvDoc.
        WPSEnv wpsenv = wpsEnvDoc.addNewWPSEnv();
      
        
        wpsenv.setId("fa98ce70-3663-11e7-a821-3417ebce41fe");
        wpsenv.setVersion("1.0.0");
        wpsenv.setMode(Mode.Enum.forString("sync"));
        wpsenv.setURL("http://geos.whu.edu.cn/wps10/WebProcessingService");
       
        process1.setExecEnv(wpsEnvDoc.getWPSEnv());
        
        //ComplexData
        DataInputType input1 = process1.addNewInput();
        input1.setId("FirstInputData");
        ReferenceType refType = input1.addNewReference();
        refType.setMimeType("application/geotiff");
        refType.setHref("http://202.114.118.181:8080/wps10/datas/image1.tif");
	
		//LiteralData
        DataInputType input2 = process1.addNewInput();
        input2.setId("Width");
        Data data = input2.addNewData();
        XmlCursor xmlCursor = data.newCursor();
        xmlCursor.setTextValue("20");
        
		try {
			document.save(new File("d:/test.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
