package com.teamap.mydemo.DataBaseHandler;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.teamap.mydemo.Entity.GlobalVariables;

public class imageOnServer {

	public static int uploadFile(String sourceFileUri) {

		String fileName = sourceFileUri;
		int serverResponseCode = 0;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {
			Log.e("uploadFile", "Source File not exist :" + sourceFileUri);
			return 0;

		} else {
			try {
				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				Log.d("Source", sourceFileUri);
				URL url = new URL("http://192.168.0.102/syncDB/UploadToServer.php");

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ fileName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {
					Log.e("Upload file to server", "Complete");
				}

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {
				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {

				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}

			return serverResponseCode;

		} // End else block
	}

	public static String uploadFileToServer(String filename, String targetUrl) {
		String response = "error";
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;

		String pathToOurFile = filename;
		String urlServer = targetUrl;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024;
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(
					pathToOurFile));

			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();

			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setChunkedStreamingMode(1024);
			// Enable POST method
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);

			String token = "anyvalye";
			outputStream
					.writeBytes("Content-Disposition: form-data; name=\"Token\""
							+ lineEnd);
			outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8"
					+ lineEnd);
			outputStream.writeBytes("Content-Length: " + token.length()
					+ lineEnd);
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(token + lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);

			String taskId = "anyvalue";
			outputStream
					.writeBytes("Content-Disposition: form-data; name=\"TaskID\""
							+ lineEnd);
			outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8"
					+ lineEnd);
			outputStream.writeBytes("Content-Length: " + taskId.length()
					+ lineEnd);
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(taskId + lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);

			String connstr = null;
			connstr = "Content-Disposition: form-data; name=\"UploadFile\";filename=\""
					+ pathToOurFile + "\"" + lineEnd;

			outputStream.writeBytes(connstr);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			System.out.println("Image length " + bytesAvailable + "");
			try {
				while (bytesRead > 0) {
					try {
						outputStream.write(buffer, 0, bufferSize);
					} catch (OutOfMemoryError e) {
						e.printStackTrace();
						response = "outofmemoryerror";
						return response;
					}
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}
			} catch (Exception e) {
				e.printStackTrace();
				response = "error";
				return response;
			}
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);

			// Responses from the server (code and message)
			int serverResponseCode = connection.getResponseCode();
			String serverResponseMessage = connection.getResponseMessage();
			Log.d("Server Response Code", serverResponseCode + " ");
			Log.d("Server Response Message ", serverResponseMessage);

			if (serverResponseCode == 200) {
				response = "true";
			} else {
				response = "false";
			}

			fileInputStream.close();
			outputStream.flush();

			connection.getInputStream();
			// for android InputStream is = connection.getInputStream();
			InputStream is = connection.getInputStream();

			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}

			String responseString = b.toString();
			Log.d("response string is", responseString); // Here is the actual
															// output

			outputStream.close();
			outputStream = null;

		} catch (Exception ex) {
			// Exception handling
			response = "error" + ex.getMessage();
			Log.d("Send file Exception", ex.getMessage() + "");
			ex.printStackTrace();
		}
		return response;
	}

	/**
	 * This function download the large files from the server
	 * 
	 * @param filename
	 * @throws java.net.MalformedURLException
	 * @throws java.io.IOException
	 */
	public static void downloadFileFromServer(String filename)
			throws MalformedURLException, IOException {
		BufferedInputStream in = null;
		FileOutputStream fout = null;
		int count;
		try {
			URL url = new URL(GlobalVariables.SERVER_URL + "uploads/" + filename);
			URLConnection conection = url.openConnection();
			conection.connect();
			// getting file length
			File mediaStorageDir;
			if (Build.VERSION.SDK_INT > 8) {
				mediaStorageDir = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			} else {
				mediaStorageDir = new File(
						Environment.getExternalStorageDirectory(), "Pictures");
			}
			if (!mediaStorageDir.exists()) {
				if (mediaStorageDir.mkdirs() || mediaStorageDir.isDirectory()) {

				}
			}

			Log.d("Url", GlobalVariables.SERVER_URL + "uploads/" + filename);

			// input stream to read file - with 8k buffer
			InputStream input = new BufferedInputStream(url.openStream(), 8192);

			// Output stream to write file

			Log.d("Out", mediaStorageDir.getPath() + "/" + filename);
			OutputStream output = new FileOutputStream(
					mediaStorageDir.getPath() + "/" + filename);

			byte data[] = new byte[1024];

			long total = 0;

			while ((count = input.read(data)) != -1) {
				output.write(data, 0, count);
			}

			// flushing output
			output.flush();

			// closing streams
			output.close();
			input.close();

		} catch (Exception e) {

		}
		/*
		 * try { URL url = new URL("http://tmas.hou.edu.vn/ms/uploads/" +
		 * filename); File mediaStorageDir = getMediaStorageDir(); if
		 * (mediaStorageDir != null) { File f = new File("file://" +
		 * mediaStorageDir.getPath() + "/" + filename); if (!f.exists()) {
		 * 
		 * } fout = new FileOutputStream();
		 * 
		 * } in = new BufferedInputStream(url.openStream()); byte data[] = new
		 * byte[1024]; int count; while ((count = in.read(data, 0, 1024)) != -1)
		 * { fout.write(data, 0, count);
		 * 
		 * } } finally { if (in != null) in.close(); if (fout != null)
		 * fout.close(); }
		 */
	}

	public static File getMediaStorageDir() {
		File mediaStorageDir;

		return null;
	}
}
