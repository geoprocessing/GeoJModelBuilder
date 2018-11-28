package com.geojmodelbuilder.xml.deserialization;

import java.io.File;
import java.io.IOException;

import net.opengis.wps.x20.DataDocument.Data;
import net.opengis.wps.x20.DataInputType;
import net.opengis.wps.x20.OutputDefinitionType;
import net.opengis.wps.x20.ReferenceType;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;

import cn.edu.whu.geos.wls.x10.ExecEnvType;
import cn.edu.whu.geos.wls.x10.LinkType;
import cn.edu.whu.geos.wls.x10.ProcessInstanceType;
import cn.edu.whu.geos.wls.x10.ProcessInstanceType.ExecType;
import cn.edu.whu.geos.wls.x10.WorkflowInstanceDocument;
import cn.edu.whu.geos.wls.x10.WorkflowInstanceType;

import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;

public class XML2Instance {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File xmlFile = new File("D:/test.xml");
//		File xmlFile = new File("D:/workflow/example/water_extraction_instance.xml");

		// Bind the instance to the generated XMLBeans types.
		
		
		try {
			//可以直接从XML流中读取
			WorkflowInstanceDocument empDoc = WorkflowInstanceDocument.Factory.parse(xmlFile);
			WorkflowInstanceType wlstype= empDoc.getWorkflowInstance();
			ProcessInstanceType[] processes = wlstype.getProcessInstanceArray();
			for (ProcessInstanceType processInstanceType : processes) {
				ExecType.Enum execType = processInstanceType.getExecType();
				
				//only support WPS
				if(!execType.equals(ExecType.Enum.forString("OGC_WPS")))
					continue;
				
				parseWPSProcess(processInstanceType);
			}
			LinkType[] links =  wlstype.getLinkArray();
			
			
//			ExecuteRequestType exeReqType = empDoc.getExecute();
		} catch (XmlException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get and print pieces of the XML instance.
		
		
		System.out.println("test");
	}
	private static WPSProcess parseWPSProcess(ProcessInstanceType processInstanceType)
	{
		processInstanceType.getExecType();
	    ExecEnvType envtype = processInstanceType.getExecEnv();

	    String name = processInstanceType.getName();
	    System.out.println("name is "+ name);
	    String identifier = processInstanceType.getIdentifier().getStringValue();
	    
	    WPSProcess wpsProcess = new WPSProcess(name);
	    
	    //the WPS address
	   // String url = wpsEnv.getURL();
	    wpsProcess.setWPSUrl("");
	    
	   
	    DataInputType[] inputs = processInstanceType.getInputArray();
	    System.out.println("the count of inputs is " + inputs.length);
	    for (DataInputType dataInputType : inputs) {
			try {
				parseInput(dataInputType);
			} catch (XmlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    
	    OutputDefinitionType[] outputs = processInstanceType.getOutputArray();
	    System.out.println(envtype);
		return wpsProcess;
	}
	
	private static void parseInput(DataInputType inputType) throws XmlException
	{
		Data data = inputType.getData();
		String value2;
		if(data!=null)
		{
			value2 = data.xmlText();
			XmlCursor xmlCursor = data.newCursor();
			String value = xmlCursor.getTextValue();
			
			System.out.println(value2);
			//String value = data.getDomNode().getNodeValue();
		}
		ReferenceType refType = inputType.getReference();
		if(refType!=null){
			String input = refType.getHref();
		}
		
	}
}
