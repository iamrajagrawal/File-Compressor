

package CShannonFanoCompressor;

public class CSFNode{
	public char ch;
	public long freq;
	public String sfCode;
	
	CSFNode(){
		ch = 0;
		freq = 0;
		sfCode = "";
		}
	CSFNode(char c,long f,String code){
		ch = c;
		freq = f;
		sfCode = code;
		}

}

