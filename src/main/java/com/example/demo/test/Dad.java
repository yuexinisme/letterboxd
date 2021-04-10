package com.example.demo.test;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.BinaryLogFileReader;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Dad implements Runnable {

    private List<String> list;
    private String name;
    public Dad(List<String> list, String name) {
        this.list = list;
        this.name = name;
    }


    @Override
    public void run() {
        list.add(name);
    }

    public static void main(String[] args) throws Exception {
        BinaryLogClient client = new BinaryLogClient("127.0.0.1", 3306, "root", "yuexinisme");
        client.setBinlogFilename("/usr/local/mysql/data/binlog.000107");

        client.registerLifecycleListener(new BinaryLogClient.LifecycleListener() {
            @Override
            public void onConnect(BinaryLogClient binaryLogClient) {
                System.out.println("OnConnect()");
            }
            @Override
            public void onCommunicationFailure(BinaryLogClient binaryLogClient, Exception e) {
                System.out.println("OnCommunicationFailure()");
                e.printStackTrace();
            }
            @Override
            public void onEventDeserializationFailure(BinaryLogClient binaryLogClient, Exception e) {
                System.out.println("OnEventDeserialize()");
            }
            @Override
            public void onDisconnect(BinaryLogClient binaryLogClient) {
                System.out.println("OnDisconnect()");
            }
        });
        client.registerEventListener(new BinaryLogClient.EventListener() {
            @Override
            public void onEvent(Event event) {
                System.out.println("event:");
                System.out.println(event.getHeader().getEventType());
                if (event.getHeader().getEventType().equals(EventType.TABLE_MAP)) {
//                    StringBuilder sb = new StringBuilder();
//                    WriteRowsEventData d = event.getData();
//                    for (Object[] row : d.getRows()) {
//                        sb.append("\n    ").append(Arrays.toString(row)).append(",");
//                    }
//                    System.out.println(sb);
                    System.out.println(event);
                }
            }
        });
        client.connect();
    }
}
