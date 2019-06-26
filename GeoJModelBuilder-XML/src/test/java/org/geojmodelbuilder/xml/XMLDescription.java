package org.geojmodelbuilder.xml;

import java.io.File;

import com.geojmodelbuilder.core.desc.IWorkflowDesc;
import com.geojmodelbuilder.xml.deserialization.XML2Description;
import com.geojmodelbuilder.xml.serialization.Description2XML;

public class XMLDescription {
	public static void main(String[] args){
		String path = "E:/workspace/water_extraction_vector_description.xml";
//		String path = "D:/Workspace/water_Extraction_workflow_instance.xml";
		XML2Description xml2Description = new XML2Description();
		IWorkflowDesc workflowDesc = xml2Description.parse(new File(path));
		
		Description2XML description2xml = new Description2XML(workflowDesc);
		String path2 = "E:/workspace/water_extraction_vector_description2.xml";
		description2xml.save(path2);
	}
}
