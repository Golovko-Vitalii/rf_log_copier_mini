import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainRFLogCopierMini {
    CopierClient cl;


    public static void main(String[] args) {
        MainRFLogCopierMini myApp = new MainRFLogCopierMini();
        myApp.cl.killProcess("Stop hackrf app");
        System.out.println("OK. Killing process success");
        try{
            System.out.println("Wait for 1 second.");
            Thread.sleep(1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().startsWith("win")) {
            System.out.println("WARNING. OS is Windows. Can't shutdown RPi. OS NAME = " + osName);
            return;
        } else if (osName.toLowerCase().startsWith("linux")) {
            myApp.shutdownRPi();
            System.out.println("Shutdown RPi. FINISH.");
            return;
        } else if (osName.toLowerCase().startsWith("mac")) {
            System.out.println("WARNING. OS is Macintosh. Can't shutdown RPi. OS NAME = " + osName);
            return;
        } else {
            return;
        }
    }

    public MainRFLogCopierMini() {
        this.cl = new CopierClient(5000, "127.0.0.1");

    }

    private void shutdownRPi() {
        try {
            Process p = Runtime.getRuntime().exec("sudo shutdown -h now");
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    //

    public class CopierClient {
        private int port;
        private InetAddress host;

        public CopierClient(int port, String host) {
            this.port = port;
            try {
                this.host = InetAddress.getByName(host);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }

        public boolean killProcess(String message) {
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket();
                byte[] data = message.getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, host, port);
                socket.send(packet);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (socket != null)
                    socket.close();
            }
            return true;
        }
    }

}
