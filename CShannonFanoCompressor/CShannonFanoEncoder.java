

package CShannonFanoCompressor;
import FileBitIO.CFileBitWriter;
import java.io.*;

public class CShannonFanoEncoder implements sfInterface {
		
	
	private String fileName,outputFilename;
	private CSFNode[] nodes = new CSFNode[MAXCHARS];
	private int distinctChars = 0;
	private long fileLen=0,outputFilelen;
	private FileInputStream fin;
	private BufferedInputStream in;	
	private String gSummary;
	private String[] hCodes = new String[MAXCHARS];;

	
	void resetFrequency(){
		
		for(int i=0;i<MAXCHARS;i++){
		nodes[i] = new CSFNode((char)i,0,"");
		}
		gSummary = "";
		distinctChars = 0;
		fileLen=0;
		}
	
	public CShannonFanoEncoder(){
		loadFile("","");
		}
	public CShannonFanoEncoder(String txt){
		loadFile(txt);
		}
	public CShannonFanoEncoder(String txt,String txt2){
		loadFile(txt,txt2);
		}
		
	public void loadFile(String txt){
		fileName = txt;
		outputFilename = txt + strExtension ;
		resetFrequency();
		}
	public void loadFile(String txt,String txt2){
		fileName = txt;
		outputFilename = txt2;
		resetFrequency();
		}
		
	public boolean encodeFile() throws Exception{
		
		if(fileName.length() == 0) return false;
		try{
		fin = new FileInputStream(fileName);
		in = new BufferedInputStream(fin);
		}catch(Exception e){ throw e; }
		
		//Frequency Analysis
  		try
		{
			fileLen = in.available();
			if(fileLen == 0) throw new Exception("File is Empty!");
			gSummary += ("Original File Size : "+ fileLen + "\n");
			
			long i=0;

			in.mark((int)fileLen);
			distinctChars = 0;
			
			while (i < fileLen)
			{		
				int ch = in.read();			
				i++;
				if(nodes[ch].freq == 0) distinctChars++;
				nodes[ch].freq++;
				
			}
			in.reset();			
		}
		catch(IOException e)
		{
			throw e;
			//return false;
		}
		
		gSummary += ("Distinct Chars : " + distinctChars + "\n");
		
/*		
		System.out.println("distinct Chars " + distinctChars);
		 //debug
		for(int i=0;i<MAXCHARS;i++){
			if(nodes[i].freq > 0)
			System.out.println(i + ")" + (char)i + " : " + nodes[i].freq);
		}
*/		
		CSFNode temp;
		boolean swapped = true;
		for(int i=0;i<MAXCHARS && swapped;i++){
			swapped = false;
			for(int j=0;j<MAXCHARS-i-1;j++)
				if(nodes[j].freq < nodes[j+1].freq){
					temp = nodes[j];
					nodes[j] = nodes[j+1];
					nodes[j+1] = temp;
					swapped = true;
					}
			}
		
		buildShannonFanoCodes(0,distinctChars,"");
/*
		//debug
		for(int i=0;i<distinctChars;i++)
			gSummary += (i + ")" + nodes[i].ch + " : " + nodes[i].freq +
								 " : " + nodes[i].sfCode  + "\n");
								 
*/
		
		for(int i=0;i<MAXCHARS;i++) hCodes[i] = "";
		for(int i=0;i<distinctChars;i++) hCodes[(int)nodes[i].ch] = nodes[i].sfCode;
		
		CFileBitWriter hFile = new CFileBitWriter(outputFilename);
		
		hFile.putString(sfSignature);
		String buf;
		buf = leftPadder(Long.toString(fileLen,2),32); //fileLen
		hFile.putBits(buf);
		buf = leftPadder(Integer.toString(distinctChars-1,2),8); //No of Encoded Chars
		hFile.putBits(buf);
		
		
		for(int i=0;i<MAXCHARS;i++){
			if(hCodes[i].length() != 0){
				buf = leftPadder(Integer.toString(i,2),8);
				hFile.putBits(buf);
				buf = leftPadder(Integer.toString(hCodes[i].length(),2),5);
				hFile.putBits(buf);
				hFile.putBits(hCodes[i]);
				}
			}
		
		long lcount = 0;
		while(lcount < fileLen){
			int ch = in.read();
			hFile.putBits(hCodes[(int)ch]);
			lcount++;
		}
		hFile.closeFile();
		outputFilelen =  new File(outputFilename).length();
		float cratio = (float)(((outputFilelen)*100)/(float)fileLen);
		gSummary += ("Compressed File Size : " + outputFilelen + "\n");
		gSummary += ("Compression Ratio : " + cratio + "%" + "\n");
		return true;
		}
	
	void buildShannonFanoCodes(int lb,int ub,String bit){
		long totalFreq = 0,sumTop=0,sumBot=0;
		
		if(lb >= ub) return;

		for(int i=lb;i<ub;i++){
			nodes[i].sfCode += bit;
			totalFreq += nodes[i].freq;
			}
		
		if(ub-lb == 1){
			nodes[lb].sfCode += "0";
			return;
			}
		if(ub-lb == 2){
			nodes[lb].sfCode += "0";
			nodes[ub-1].sfCode += "1";
			return;
			}
		
		int mid = ub - 1;
		long minDiff = totalFreq;
		int minMid = mid;
		
		while(mid >=lb ){
			sumBot = 0;
			for(int i=mid;i<ub;i++)
			sumBot += nodes[i].freq;
			
			sumTop  = totalFreq - sumBot;
			
			if(minDiff > Math.abs(sumBot - sumTop)){
				minDiff = Math.abs(sumBot - sumTop);
				minMid = mid;
				}
			
			mid--;
			}
		
			buildShannonFanoCodes(lb,minMid,"0");
			buildShannonFanoCodes(minMid,ub,"1");
		
		
		
		
		}
	
	
	String leftPadder(String txt,int n){
		while(txt.length() < n )
			txt =  "0" + txt;
		return txt;
		}
	
	String rightPadder(String txt,int n){
		while(txt.length() < n )
			txt += "0";
		return txt;
		}

	public String getSummary(){
		return gSummary;
		}
	}

