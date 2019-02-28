package fasko.wifilogger;

import android.os.AsyncTask;
import com.jcraft.jsch.*;

public class UploadToServer extends AsyncTask<String, Void, String>{

    protected String doInBackground(String... params) {
        try {
            String user ="wouldn't"; // username for remote host
            String password ="you"; // password of the remote host

            String host = "like"; // remote host address
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking","no"); //todo, make this verify
            session.connect();

            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            sftpChannel.put("/storage/emulated/0/Android/data/fasko.wifilogger/files/Documents/"
                    +"scan"+params[0]+".txt", "to know");
            sftpChannel.disconnect();
            session.disconnect();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
