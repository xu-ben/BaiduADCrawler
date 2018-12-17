import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Commons {
	public static String getTextFromFile(File f) throws IOException {
		int len = (int) f.length();
		FileInputStream fis = new FileInputStream(f);
		InputStreamReader isr = new InputStreamReader(fis, "utf-8");
		String res = getTextFromStreamReader(isr, len);
		isr.close();
		return res;
	}

	public static String getTextFromStreamReader(InputStreamReader isr, int contentSize) throws IOException {
		if (isr == null) {
			System.err.println("null argument");
			return null;
		}
		if (contentSize <= 0) {
			return null;
		}
		BufferedReader br = new BufferedReader(isr);
		char[] content = new char[contentSize];
		int textLen = br.read(content);
		br.close();
		int offset = 0;
		/*
		 * 去掉BOM头无效字符
		 */
		if (65279 == (int) content[0]) {
			offset = 1;
		}
		return String.valueOf(content, offset, textLen - offset);
	}

	public static String ExecCmdInDir(String cmd, String dir) throws IOException {
		if (dir == null || dir.trim().equals("")) {
			throw new FileNotFoundException();
		}
		File d = new File(dir);
		if (!d.exists() || !d.isDirectory()) {
			throw new IOException("not dir");
		}
		String[] cmds = { "/bin/sh", "-c", "" };
		cmds[2] = cmd;
		Process proc = Runtime.getRuntime().exec(cmds, null, d);
		InputStreamReader isr = new InputStreamReader(proc.getInputStream());
		BufferedReader br = new BufferedReader(isr);

		StringBuilder sb = new StringBuilder();
		char[] buf = new char[512];
		int len = 0;
		while ((len = br.read(buf)) > 0) {
			sb.append(buf, 0, len);
		}
		br.close();
//		System.out.println(sb);
		return sb.toString();
	}
	
}
