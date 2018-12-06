package com.geojmodelbuilder.xml.serialization;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.opengis.ows.x20.CodeType;
import net.opengis.ows.x20.LanguageStringType;
import net.opengis.wps.x20.DataDocument.Data;
import net.opengis.wps.x20.DataInputType;
import net.opengis.wps.x20.DataTransmissionModeType;
import net.opengis.wps.x20.OutputDefinitionType;
import net.opengis.wps.x20.ReferenceType;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlOptions;

import cn.edu.whu.geos.wls.x10.ExtendedLinkType;
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
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setUseDefaultNamespace();
		
		Map nsMap = new HashMap(); 
		nsMap.put("http://geos.whu.edu.cn/wls/1.0","wls");
		nsMap.put("http://www.opengis.net/wps/2.0","wps");
		nsMap.put( "http://www.opengis.net/ows/2.0","ows");
		nsMap.put("http://www.w3.org/1999/xlink","xlink");
		xmlOptions.setSaveSuggestedPrefixes(nsMap);
		xmlOptions.setSavePrettyPrint();
		xmlOptions.setSaveAggressiveNamespaces();
		
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
	
        //ComplexData, second
        DataInputType input2 = process1.addNewInput();
        input2.setId("SecondInputData");
        ReferenceType refType2 = input2.addNewReference();
        refType2.setMimeType("application/geotiff");
        refType2.setHref("http://202.114.118.181:8080/wps10/datas/image2.tif");
        
        OutputDefinitionType output = process1.addNewOutput();
        //OutputDescriptionType output = outptu
        output.setId("OutputData");
        
        //transmission{value,reference}
        output.setTransmission(DataTransmissionModeType.Enum.forString("reference"));
        
        ProcessInstanceType process2 = workflowInstance.addNewProcessInstance();
		process2.setExecType(ExecType.Enum.forString("OGC_WPS"));
		
		String process2_id = "http://geos.whu.edu.cn/wps10/RasterBinaryProcess/3995f2c0-3663-11e7-9d44-3417ebce41fe";
		CodeType process2_idty =  process2.addNewIdentifier();
		process2_idty.setStringValue(process2_id);
		
		process2.setName("RasterBinaryProcess");
		process2.setExecEnv(wpsEnvDoc.getWPSEnv());
		
		DataInputType input21 = process2.addNewInput();
		input21.setId("InputData");
        ReferenceType refType21 = input21.addNewReference();
        refType21.setMimeType("application/geotiff");
        refType21.setHref("843f0661-3661-11e7-b6bc-3417ebce41fe#OutputData");
		
        OutputDefinitionType output2 = process2.addNewOutput();
        //OutputDescriptionType output = outptu
        output2.setId("OutputData");
        //transmission{value,reference}
        output2.setTransmission(DataTransmissionModeType.Enum.forString("reference"));
        
		ExtendedLinkType link = workflowInstance.addNewLink();
		link.setSourceProcess(process1.getIdentifier().getStringValue());
		link.setSourceVariable("OutputData");
		link.setTargetProcess(process2.getIdentifier().getStringValue());
		link.setTargetVariable("InputData");
		//LiteralData
        
		/*
        DataInputType input3 = process2.addNewInput();
        input3.setId("Width");
        Data data = input3.addNewData();
        XmlCursor xmlCursor = data.newCursor();
        xmlCursor.setTextValue("20");
        */
		try {
			document.save(new File("d:/test.xml"),xmlOptions);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	   //System.out.println(document.xmlText(xmlOptions));
		
	}
}
