package ben.tools.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Commons {

	public static String chromeUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";

	/**
	 * 在当前目录(".")下，阻塞式执行cmd命令，返回命令在stdout中的输出内容, 如果没有输出，则为""，不会为null
	 * @param cmd
	 * @return
	 * @throws IOException
	 */
	public static String execCmd(String cmd) throws IOException {
		return execCmdInDir(cmd, ".", -1);
	}

	/**
	 * 执行cmd命令，如果timeout为正，则阻塞式执行，否则，最多等待timeout秒即返回
	 * @param cmd
	 * @param dir
	 * @param timeout 单位为秒
	 * @return 所执行的命令在stdout中输出的内容, 如果没有输出，则为""，不会为null
	 * @throws IOException
	 */
	public static String execCmdInDir(String cmd, String dir, int timeout) throws IOException {
		if (cmd == null || cmd.trim().equals("")) {
			throw new IOException("cmd error");// todo 优化
		}
		if (dir == null || dir.trim().equals("")) {
			throw new FileNotFoundException();
		}
		File d = new File(dir);
		if (!d.exists() || !d.isDirectory()) {
			throw new FileNotFoundException("not dir");
		}
		StringBuilder stdout = new StringBuilder();
		StringBuilder stderr = new StringBuilder();
		if (timeout < 0) {
			myExecSync(cmd, d, stdout, stderr);
		} else {
			myExecAsync(cmd, d, timeout, stdout, stderr);
		}
		return stdout.toString();
	}

	/**
	 * 同步执行cmd命令，方法会阻塞至运行的子进程结束
	 * @param cmd
	 * @param dir
	 * @param out
	 * @param err
	 * @throws IOException
	 */
	private static void myExecSync(String cmd, File dir, StringBuilder out, StringBuilder err) throws IOException {
		String[] cmds = { "/bin/sh", "-c", cmd };
		Process proc = null;
		BufferedReader brout = null, brerr = null;
		char[] buf = new char[512];
		int len = 0;
		try {
			proc = Runtime.getRuntime().exec(cmds, null, dir);
			brout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			brerr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			while ((len = brout.read(buf)) > 0) {
				out.append(buf, 0, len);
			}
			while ((len = brerr.read(buf)) > 0) {
				err.append(buf, 0, len);
			}
		} catch (IOException ioe) {
		} finally {
			if (proc != null) {
				proc.destroy();
			}
			if (brout != null) {
				brout.close();
			}
			if (brerr != null) {
				brerr.close();
			}
		}
	}

	/**
	 * 异步执行cmd命令，最多等待timeout秒
	 * @param cmd
	 * @param dir
	 * @param timeout 单位：秒
	 * @param out
	 * @param err
	 * @return
	 * @throws IOException
	 */
	private static boolean myExecAsync(String cmd, File dir, int timeout, StringBuilder out, StringBuilder err) throws IOException {
		long start = System.currentTimeMillis();
		String[] cmds = { "/bin/sh", "-c", cmd };
		Process proc = null;
		BufferedReader brout = null, brerr = null;
		char[] buf = new char[512];
		int len = 0;
		try {
			proc = Runtime.getRuntime().exec(cmds, null, dir);
			brout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			brerr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			while (true) {
				while (brout.ready()) {
					if ((len = brout.read(buf)) > 0) {
						out.append(buf, 0, len);
					}
				}
				while (brerr.ready()) {
					if ((len = brerr.read(buf)) > 0) {
						err.append(buf, 0, len);
					}
				}
				try {
					proc.exitValue();
					break;
				} catch (IllegalThreadStateException e) {// 进程未结束会到这
				}
				if (System.currentTimeMillis() - start > timeout * 1000) {
//					System.err.println("命令执行超时退出.\n");
					return false;
				}
				Thread.sleep(50);
			}
		} catch (IOException e) {
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (proc != null) {
				proc.destroy(); //
				proc = null;
			}
			if (brout != null) {
				brout.close();
			}
			if (brerr != null) {
				brerr.close();
			}
		}
		return true;
	}

}
