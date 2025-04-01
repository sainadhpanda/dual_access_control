package Networks;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class DRIVE_Network {
    private FTPClient client;
    private FileInputStream fis;
    private boolean status;

    public DRIVE_Network() {
        client = new FTPClient();
    }

    public boolean upload(File file) {
        try {
            System.out.println("Connecting to FTP server...");
            client.connect("ftp.drivehq.com");
            
            // Check server reply
            int reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                System.out.println("FTP server refused connection.");
                return false;
            }

            System.out.println("Logging in...");
            if (!client.login("cloudcrypt5", "cloud@12345")) {  // Update with your new password
                System.out.println("FTP Login Failed. Check username/password.");
                return false;
            }

            System.out.println("Login successful.");

            // Set file transfer mode
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.enterLocalPassiveMode();

            // Check if file exists
            if (!file.exists() || !file.isFile()) {
                System.out.println("File does not exist or is not a regular file.");
                return false;
            }

            // Upload file
            System.out.println("Uploading file: " + file.getAbsolutePath());
            fis = new FileInputStream(file);
            status = client.storeFile("/" + file.getName(), fis);

            if (status) {
                System.out.println("File uploaded successfully.");
            } else {
                System.out.println("File upload failed. Server response: " + client.getReplyString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
                if (client.isConnected()) {
                    System.out.println("Logging out and disconnecting...");
                    client.logout();
                    client.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return status;
    }
}
