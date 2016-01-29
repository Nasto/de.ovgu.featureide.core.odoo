package de.ovgu.featureide.code.odoo.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.commands.common.NotDefinedException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.ovgu.featureide.code.odoo.Models.ConfigurationSection;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardModel;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardPageModel;
import de.ovgu.featureide.code.odoo.Models.ConfigurationWizardPageModel.logicalOperator;
import de.ovgu.featureide.code.odoo.Models.SimpleFeature;

public class surveyConfigurationParser {

	private static String projectName = "projectName";
	private static String sectionList = "sections";
	private static String section = "section";
	private static String sectionName = "name";
	private static String sectionDescription = "description";
	private static String sectionIdAttribute = "id";
	private static String surveyPageList = "surveyPages";
	private static String page = "page";
	private static String pageSectionIdAttribute = "sectionId";
	private static String question = "question";
	private static String answerList = "answers";
	private static String answer = "answer";
	private static String answerTypeAttribute = "type";
	private static String pageAnswerName = "name";
	private static String pageAnswerDescription = "description";
	private static String pageAnswerDependencieList = "dependencies";
	private static String pageAnswerDependencieElement = "feature";
	
	public static ConfigurationWizardModel parseConfiguration(File inputFile){
		ConfigurationWizardModel result = new ConfigurationWizardModel();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();		
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			result.setProjectName(doc.getElementsByTagName(projectName).item(0).getTextContent());
			
			// Gets the sections
			Element sectionListElement = (Element) doc.getElementsByTagName(sectionList).item(0);
			NodeList sectionNodes = sectionListElement.getElementsByTagName(section);
			for(int i = 0; i<sectionNodes.getLength(); i++){
				Node nNode = sectionNodes.item(i);
				ConfigurationSection section = new ConfigurationSection();
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element sectionElement = (Element) nNode;
	               section.setId(Integer.parseInt(sectionElement.getAttribute(sectionIdAttribute)));
	               section.setName(sectionElement.getElementsByTagName(sectionName).item(0).getTextContent());
	               section.setDescripton(sectionElement.getElementsByTagName(sectionDescription).item(0).getTextContent());
	               result.addSection(section);
	            }				
			}		
			
			// Gets the pages
			Element surveyPageListElement = (Element) doc.getElementsByTagName(surveyPageList).item(0);
			NodeList pageNodes = surveyPageListElement.getElementsByTagName(page);
			for(int i = 0; i<pageNodes.getLength(); i++){
				Node nNode = pageNodes.item(i);
				ConfigurationWizardPageModel page = new ConfigurationWizardPageModel();
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element pageElement = (Element) nNode;
	               page.sectionId = Integer.parseInt(pageElement.getAttribute(pageSectionIdAttribute));
	               page.question = pageElement.getElementsByTagName(question).item(0).getTextContent();
	               
	               // Gets the answerList               
	               Element answerListElement = (Element) pageElement.getElementsByTagName(answerList).item(0);
	               //Get the answer type
	               switch (ConfigurationWizardPageModel.logicalOperator.valueOf(answerListElement.getAttribute(answerTypeAttribute))){
		               case and:{
		            	   page.featureOperator = logicalOperator.and;
		            	   break;
		               }
		               case alternative:{
		            	   page.featureOperator = logicalOperator.alternative;
		            	   break;
		               }
		               default:{
		            	   throw new NotDefinedException("This answertype is not supported: "+ answerListElement.getAttribute(answerTypeAttribute));
		               }
	               }
	               // Gets the answers
	               NodeList answerNodes = surveyPageListElement.getElementsByTagName(answer);
	               for(int j = 0; j<answerNodes.getLength(); j++){
	   				Node mNode = answerNodes.item(j);	   				
	   				if (mNode.getNodeType() == Node.ELEMENT_NODE) {
	   	               Element asnwerElement = (Element) mNode;
	   	               SimpleFeature feature = new SimpleFeature(asnwerElement.getElementsByTagName(pageAnswerName).item(0).getTextContent(),
	   	            		   asnwerElement.getElementsByTagName(pageAnswerDescription).item(0).getTextContent());
	   	               
	   	               // Gets the dependencies
		               Element dependencyListElement = (Element) asnwerElement.getElementsByTagName(pageAnswerDependencieList).item(0);
		               if(dependencyListElement != null){
		   	               NodeList dependencyNodes = dependencyListElement.getElementsByTagName(pageAnswerDependencieElement);
			               for(int k = 0; k<dependencyNodes.getLength(); k++){
			            	   Node oNode = answerNodes.item(j);	   				
				   				if (oNode.getNodeType() == Node.ELEMENT_NODE) {
				   	               Element dependencyElement = (Element) nNode;
				   	               feature.addDependency(dependencyElement.getTextContent());
				   				}		            	   
			               }
		               }
		               page.addFeature(feature);		           
	   				}
	               }	               
	               result.addPage(page);
	            }
			}
		} catch (ParserConfigurationException | SAXException | IOException | NotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;		
	}
}
