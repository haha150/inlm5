package org.inlm5.server.start;

import org.inlm5.server.net.Server;

public class ServerMain {
    public static void main(String[] args) {
        Server s = new Server();
        s.start();
    }
}
