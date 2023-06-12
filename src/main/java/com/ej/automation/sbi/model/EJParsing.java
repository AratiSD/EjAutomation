package com.ej.automation.sbi.model;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;

public class EJParsing {
	
	private static JSONObject ejDescriptors = new JSONObject();
	
	public static enum Operation {
		FoundOnSameLine, SetValueIfFound, FoundOnDifferentLine
	}
	
	public static Hashtable<String,List<TransactionDescriptor>> SetTransactionDescriptors(String bank) {
		Hashtable<String,List<TransactionDescriptor>> transactionDescriptorTable = new Hashtable<String,List<TransactionDescriptor>>();
		
		List<TransactionDescriptor> transactionDescriptors = new ArrayList<TransactionDescriptor>();
		try {
			String bankId = bank.substring(2, bank.length()-2);
			String EJDescriptorFileName="D:\\CLMWorkSpace\\FileXAPIWeb_MUM\\src\\"+bankId+".xls";
			//String EJDescriptorFileName=Constants.HOME_DIR+"EJDescriptors/"+bankId+".xls";
			InputStream inputfile = new FileInputStream(EJDescriptorFileName);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputfile);
			
			int sheetCount = hssfWorkbook.getNumberOfSheets();
			for(int j = 0; j<sheetCount;j++){
				HSSFSheet sheet = hssfWorkbook.getSheetAt(j);
				String sheetname = hssfWorkbook.getSheetName(j);
				
				int rowCount = sheet.getPhysicalNumberOfRows();
				

				for (int i = 2; i < rowCount; i++) {
					//System.out.println("   row number     "+i);
					TransactionDescriptor transactionDescriptor = new TransactionDescriptor();
					HSSFRow row = sheet.getRow(i);
					HSSFCell parameterCell 			= row.getCell((short) 1);
					HSSFCell operationCell 			= row.getCell((short) 2);
					HSSFCell containsCell 			= row.getCell((short) 3);
					HSSFCell excludesCell			= row.getCell((short) 4);
					HSSFCell containsEitherCell 	= row.getCell((short) 5);
					HSSFCell splitOnCell 			= row.getCell((short) 6);
					HSSFCell foundAtIndexCell 		= row.getCell((short) 7);
					HSSFCell foundOnLineCell 		= row.getCell((short) 8);
					HSSFCell trimMultipleSpacesCell = row.getCell((short) 9);
					HSSFCell replaceStringCell 		= row.getCell((short) 10);
					HSSFCell replaceWithCell 		= row.getCell((short) 11);
					HSSFCell substringStartCell 	= row.getCell((short) 12);
					HSSFCell substringEndCell 		= row.getCell((short) 13);
					HSSFCell valueCell 				= row.getCell((short) 14);
					HSSFCell dontWriteIfExistCell 	= row.getCell((short) 15);

					if(parameterCell!=null)
					if(parameterCell.getCellType()==1){
						String parameter = parameterCell.getRichStringCellValue().toString();
						transactionDescriptor.setParameter(parameter);
					}
					if(operationCell!=null)
					if(operationCell.getCellType()==1){
						String operation = operationCell.getRichStringCellValue().toString();
						transactionDescriptor.setOperation(operation);
					}
					if(containsCell != null)
					if(containsCell.getCellType()==1){
						String containsString = containsCell.getRichStringCellValue().toString();
						String[] contains = getStringArray(containsString);
						transactionDescriptor.setContains(contains);				
					}
					if(excludesCell!=null)
					if(excludesCell.getCellType()==1){
						String excludesString = excludesCell.getRichStringCellValue().toString();
						String[] excludes = getStringArray(excludesString);
						transactionDescriptor.setExcludes(excludes);				
					}
					if(containsEitherCell!=null)
					if(containsEitherCell.getCellType()==1){
						String containsEitherString = containsEitherCell.getRichStringCellValue().toString();
						String[] containsEither = getStringArray(containsEitherString);
						transactionDescriptor.setContainsEither(containsEither);				
					}
					if(splitOnCell!=null)
					if(splitOnCell.getCellType()==1){
						String splitOn = splitOnCell.getRichStringCellValue().toString().replace("\"","");
						transactionDescriptor.setSplitOn(splitOn);
					}
					if(foundAtIndexCell!=null)
					if(foundAtIndexCell.getCellType()==0){
						int foundAtIndex = (int) foundAtIndexCell.getNumericCellValue();
						transactionDescriptor.setFoundAtIndex(foundAtIndex);
					}
					if(foundOnLineCell!=null)
					if(foundOnLineCell.getCellType()==0){
						int foundOnLine = (int) foundOnLineCell.getNumericCellValue();
						transactionDescriptor.setFoundOnLine(foundOnLine);
					}
					if(trimMultipleSpacesCell!=null)
					if(trimMultipleSpacesCell.getCellType()==1){
						String trimMultipleSpacesString = trimMultipleSpacesCell.getRichStringCellValue().toString();
						boolean trimMultipleSpaces = trimMultipleSpacesString.equalsIgnoreCase("Yes")?true:false;
						transactionDescriptor.setTrimMultipleSpaces(trimMultipleSpaces);
					}
					if(replaceStringCell!=null)
					if(replaceStringCell.getCellType()==1){
						String replaceString = replaceStringCell.getRichStringCellValue().toString().replace("\"","");
						transactionDescriptor.setReplaceThis(replaceString);
					}
					if(replaceWithCell!=null)
					if(replaceWithCell.getCellType()==1){
						String replaceWith = replaceWithCell.getRichStringCellValue().toString().replace("\"","");
						transactionDescriptor.setReplaceWith(replaceWith);
					}
					if(substringStartCell!=null)
					if(substringStartCell.getCellType()==0){
						int substringStart = (int) substringStartCell.getNumericCellValue();
						transactionDescriptor.setSubstringStart(substringStart);
					}
					if(substringEndCell!=null)
					if(substringEndCell.getCellType()==0){
						int substringEnd = (int) substringEndCell.getNumericCellValue();
						transactionDescriptor.setSubstringEnd(substringEnd);
					}
					if(valueCell!=null)
					if(valueCell.getCellType()==1){
						String value= valueCell.getRichStringCellValue().toString().replace("\"","");
						transactionDescriptor.setValue(value);
					}
					if(dontWriteIfExistCell!=null)
					if(dontWriteIfExistCell.getCellType()==1){
						String dontWriteIfExistString = dontWriteIfExistCell.getRichStringCellValue().toString();
						boolean dontWriteIfExist = dontWriteIfExistString.equalsIgnoreCase("Yes")?true:false;
						transactionDescriptor.setDontWriteIfExists(dontWriteIfExist);
					}
					
					if(transactionDescriptor.getOperation()!=null && transactionDescriptor.getParameter()!=null)					
						transactionDescriptors.add(transactionDescriptor);
					
					
					
				}
				
				transactionDescriptorTable.put(sheetname, transactionDescriptors);
			}
			inputfile.close();
			
			

		} catch (Exception ex) {
			ex.printStackTrace();
			FileXLogger.logEjAutomationError(ex);
		}
		return transactionDescriptorTable;
	}
	
	private static String[] getStringArray(String input){
		String[] strings = input.split(",");
		for(int i=0;i<strings.length;i++){
			strings[i] = strings[i].replace("\"", "");
		}
		return strings;
	}
	
	
	
	public static Map<String,String> parseTransactionParameters(List<String> transaction, String make){
		Map<String,String> transactionParams = new HashMap<String,String>();
		List<TransactionDescriptor> transactionDescriptors = Configuration.transactionDescriptorTable.get(make);
		try{
			for(int i=0;i<transaction.size();i++){
				
	            for(TransactionDescriptor transactionDescriptor : transactionDescriptors){
	            	transactionParams = applyTransformations(transaction,i,transactionParams,transactionDescriptor);
	            }
				
			}
		}catch(Exception ex){
			ex.printStackTrace();
			FileXLogger.logEjAutomationError(ex);
		}
		return transactionParams;
	}

	private static Map<String, String> applyTransformations(List<String> transaction, int index, Map<String, String> transactionParams, TransactionDescriptor transactionDescriptor) {
		// TODO Auto-generated method stub
        try {     
        	String splitOn="", parameter="", value="";
        	Boolean conditionsMet;
        	int foundAtIndex, foundOnLine;
        	
        	String operationString = transactionDescriptor.getOperation();
        	Operation operation = Operation.valueOf(operationString);
        	String transactionLine = transaction.get(index);
        	System.out.println("    index             "+index);
        	System.out.println("   transactionLine    "+transactionLine);
        	Boolean dontWriteIfExist = (Boolean) transactionDescriptor.isDontWriteIfExists();
        	switch(operation){
        		case FoundOnSameLine:
        			splitOn = transactionDescriptor.getSplitOn();
        			foundAtIndex = transactionDescriptor.getFoundAtIndex();
        			parameter = transactionDescriptor.getParameter();
        			transactionLine= CleanTransactionLine(transactionDescriptor, transactionLine);
        			conditionsMet = CheckConditions(transactionDescriptor, transactionLine);
        			        			        			
        			if(conditionsMet){
        				
        				if(splitOn!=null){
        					String[] lineSplit = transactionLine.split(splitOn);
            				value=lineSplit[foundAtIndex].trim();
        				}
        				else if(splitOn==null){
        					value=transactionLine;
        				}
        				value = CleanTransactionValue(transactionDescriptor, value);
        				if(dontWriteIfExist!=null){
            				if(dontWriteIfExist){
            					if(transactionParams.get(parameter)!=null){
            						break;
            					}
            				}
            			}  
        				transactionParams.put(parameter, value);
        				
        			}
        			break;
        		case SetValueIfFound:
        			parameter = transactionDescriptor.getParameter();
        			transactionLine= CleanTransactionLine(transactionDescriptor, transactionLine);
        			conditionsMet = CheckConditions(transactionDescriptor, transactionLine);
        			value= transactionDescriptor.getValue();
        			
        			
        			if(conditionsMet && value!=null){
        				if(dontWriteIfExist!=null){
            				if(dontWriteIfExist){
            					if(transactionParams.get(parameter)!=null){
            						break;
            					}
            				}
            			}
        				transactionParams.put(parameter, value);
        			}
        			break;
        		case FoundOnDifferentLine:
        			splitOn = transactionDescriptor.getSplitOn();
        			foundAtIndex = transactionDescriptor.getFoundAtIndex();
        			parameter = transactionDescriptor.getParameter();
        			foundOnLine = transactionDescriptor.getFoundOnLine();
        			
        			transactionLine= CleanTransactionLine(transactionDescriptor, transactionLine);
        			conditionsMet = CheckConditions(transactionDescriptor, transactionLine);
        			if(conditionsMet){
        				value=transaction.get(index);
        				if(foundOnLine!=0){
        					if(transaction.size()>=foundOnLine+index){
        						value=transaction.get(foundOnLine+index);
        					}
        				}
        				value = CleanTargetLine(transactionDescriptor,value);
        				if(splitOn!=null){
        					String[] lineSplit = value.split(splitOn);
            				value=lineSplit[foundAtIndex];
        				}
        				value = value.trim();
        				value = CleanTransactionValue(transactionDescriptor, value);
        				if(dontWriteIfExist!=null){
            				if(dontWriteIfExist){
            					if(transactionParams.get(parameter)!=null){
            						break;
            					}
            				}
            			}
        				transactionParams.put(parameter, value);
        			}
        			break;
        	}
            
        } catch (Exception e) {
            e.printStackTrace();
            FileXLogger.logEjAutomationError(e);
        }
		return transactionParams;
	}
	
	private static String CleanTargetLine(TransactionDescriptor transactionDescriptor,String value) {
		Boolean trimMultipleSpaces	= transactionDescriptor.isTrimMultipleSpaces();
		//JSONObject replaceWith = (JSONObject) transactionDescriptor.get("replaceTargetLineWith");
		
		if(trimMultipleSpaces!=null){
			if(trimMultipleSpaces == true)
				value = value.trim().replaceAll(" +", " ");
		}
		
		/*if(replaceWith != null){
			String replace = (String) replaceWith.get("replace");
			String with = (String) replaceWith.get("with");
			if(replace != null && with != null)
				value=value.replaceAll(replace, with);
		}*/
		
		return value;
	}

	private static Boolean CheckConditions(TransactionDescriptor transactionDescriptor, String transactionLine ){
		boolean conditionsMet=true;
		String[] 	containsList 		= transactionDescriptor.getContains(); 
		String[] 	excludesList 		= transactionDescriptor.getExcludes();
		String[] 	containsEitherList 	= transactionDescriptor.getContainsEither();
		if(containsList != null){
			Boolean contains = containsWords(transactionLine,containsList);
			conditionsMet = conditionsMet?contains:conditionsMet;
		}
		if(excludesList != null){
			Boolean excludes = excludesWords(transactionLine,excludesList);
			conditionsMet = conditionsMet?excludes:conditionsMet;
		}
		if(containsEitherList != null){
			Boolean containsEither = containsEitherWords(transactionLine,containsEitherList);
			conditionsMet = conditionsMet?containsEither:conditionsMet;
		}
		return conditionsMet;
	}
	
	private static String CleanTransactionLine(TransactionDescriptor transactionDescriptor, String transactionLine){
		Boolean 	trimMultipleSpaces	= transactionDescriptor.isTrimMultipleSpaces();
		String replace = transactionDescriptor.getReplaceThis();
		String with = transactionDescriptor.getReplaceWith();
		
		if(trimMultipleSpaces!=null){
			if(trimMultipleSpaces == true)
				transactionLine = transactionLine.trim().replaceAll(" +", " ");
		}
		if(replace != null && with != null){
			transactionLine=transactionLine.replaceAll(replace, with);
		}
		
		return transactionLine;
	}
	
	private static String CleanTransactionValue(TransactionDescriptor transactionDescriptor, String value){
		int start = transactionDescriptor.getSubstringStart();
		int end = transactionDescriptor.getSubstringEnd();
		if(end!=0)
			value = value.substring(start,end);
		else
			value =value.substring(start);
		return value;
	}
	
	private static Boolean excludesWords(String inputString,String[] items) {
		boolean found = true;
	    for (String item : items) {
	        if (inputString.contains(item)) {
	            found = false;
	            break;
	        }
	    }
	    return found;
	}

	public static boolean containsWords(String inputString, String[] items) {
	    boolean found = true;
	    for (String item : items) {
	        if (!inputString.contains(item)) {
	            found = false;
	            break;
	        }
	    }
	    return found;
	}
	
	public static boolean containsEitherWords(String inputString, String[] items) {
	    boolean found = false;
	    for (String item : items) {
	        if (inputString.contains(item)) {
	            found = true;
	            break;
	        }
	    }
	    return found;
	}
	
	/*public static void main(String[] args){
	try{
		//List<TransactionDescriptor> transactionDescriptors = SetTransactionDescriptors("CANARABNK","NCR");
		
		byte[] readBuffer;
		String losgFileData="";
		String losgDistFile = "C:\\Users\\expleo_13\\Desktop\\EJ automation\\testparse.txt";
		String bank = "UBIBNK";
		String make = "NCR";
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader("D:\\CLMWorkSpace\\FileXAPIWeb_MUM\\src\\EJDescriptors.json"));
		ejDescriptors = (JSONObject) obj;
		JSONObject makeDescriptors = new JSONObject();
		makeDescriptors =  (JSONObject)((JSONObject) ejDescriptors.get(bank)).get(make);
		String transactionStartDelimiter = (String) makeDescriptors.get("transactionStartDelimiter");
		String transactionEndDelimiter = (String) makeDescriptors.get("transactionEndDelimiter");
		
		int fileSize = Integer.parseInt(String.valueOf(new File(losgDistFile).length()));
		final FileInputStream fis = new FileInputStream(losgDistFile);
		readBuffer = new byte[fileSize];
		while (fis.read(readBuffer) != -1) {
			losgFileData = M24Utility.ba2s(readBuffer);
			break;
		}
		fis.close();
		
		// Need to make take care of \n and \r
		String[] EJbuffer= losgFileData.split("\n");
		
		int parsedUntill;
		
		String parsedLines="0";
		
		if(parsedLines==null || parsedLines==""){
			parsedUntill = 0;
		}
		else{
			parsedUntill = Integer.parseInt(parsedLines);
		}
		int i= parsedUntill;
		
		List<List<String>> transactions = new ArrayList<List<String>>();
		while(i<EJbuffer.length){
			if(EJbuffer[i].contains(transactionStartDelimiter)){
				List<String> temp = new ArrayList<String>();
				while(!EJbuffer[i-1].contains(transactionEndDelimiter)&&i<EJbuffer.length){
					if(EJbuffer[i].contains(transactionEndDelimiter)){
						parsedUntill=i;
						transactions.add(temp);
						i++;
						break;
					}
					temp.add(EJbuffer[i]);
					i++;
				};
			}else{
				i++;
			}
			if(i<EJbuffer.length){
				if(EJbuffer[i].contains(transactionStartDelimiter) && EJbuffer[i-1].contains(transactionEndDelimiter)){
					i++;
				}
			}
			
		}
		List<Map<String,String>> transactionList = new ArrayList<Map<String,String>>();
		for(int j=0;j<transactions.size();j++){
			
			List<String> transaction=transactions.get(j);
			//transactionList.add(parseTransactionParameters(transaction, transactionDescriptors));
		}
	}catch(Exception ex){
		
	}
	
}*/

}
