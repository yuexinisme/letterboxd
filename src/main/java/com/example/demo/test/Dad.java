package com.example.demo.test;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

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

    public static void main(String[] args) {
        Deployment deployment = new DeploymentBuilder()
                .withNewMetadata()
                .withName("nginx")
                .endMetadata()
                .withNewSpec()
                .withReplicas(1)
                .withNewTemplate()
                .withNewMetadata()
                .addToLabels("app", "nginx")
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName("nginx")
                .withImage("nginx")
                .addNewPort()
                .withContainerPort(80)
                .endPort()
                .endContainer()
                .endSpec()
                .endTemplate()
                .withNewSelector()
                .addToMatchLabels("app", "nginx")
                .endSelector()
                .endSpec()
                .build();
        KubernetesClient client = new DefaultKubernetesClient();
        deployment = client.apps().deployments().inNamespace("NAMESPACE").create(deployment);
    }
}
