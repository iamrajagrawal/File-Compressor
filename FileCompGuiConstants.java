public interface FileCompGuiConstants {
String[] algorithmNamesArray = {
"Huffman Compression",
"Shannon Fano Compression",
"GZip Compression",
"RLE Compressor",
"LZW Compressor"
};
String[] extensionArray = {
".huf",
".sfe",
".gz",
".rle",
".lzw"
};
final int COMP_HUFFMAN = 0;
final int COMP_SHANNONFANO = 1;
final int COMP_GZIP = 2;
final int COMP_COSMO = 3;
final int COMP_JBC = 4;
final int COMP_RLE = 5;
final int COMP_LZW = 6;
final int COMPRESS = 32;
final int DECOMPRESS = 33;
}