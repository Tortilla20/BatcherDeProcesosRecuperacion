/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.batcherdeprocesosrecuperacion.monitor;

import com.mycompany.batcherdeprocesosrecuperacion.resource.ResourceManager;
import com.mycompany.batcherdeprocesosrecuperacion.storage.JobStorage;
import com.mycompany.batcherdeprocesosrecuperacion.model.Job;

/**
 *
 * @author duroi
 */

public class Monitor {

    private ResourceManager rm;

    public Monitor(ResourceManager rm) {
        this.rm = rm;
    }

    public void start(JobStorage storage) {

        new Thread(() -> { while (true) {
                try {
                    Thread.sleep(1000); // cada segundo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                printState(storage);
            }
        }).start();
    }

    private void printState(JobStorage storage) {

        System.out.println("\n===== BATCHER MONITOR =====");

        // Recursos
        int usedCores = rm.getTotalCores() - rm.getAvailableCores();
        int usedMem = rm.getTotalMemMb() - rm.getAvailableMemMb();

        System.out.println("CPU: " + usedCores + "/" + rm.getTotalCores());
        System.out.println("RAM: " + usedMem + "/" + rm.getTotalMemMb() + " MB");

        // Colas
        System.out.println("READY: " + storage.getReadyJobs().size());
        System.out.println("WAITING: " + storage.getWaitingJobs().size());
        System.out.println("RUNNING: " + storage.getRunningJobs().size());
        System.out.println("DONE: " + storage.getDoneJobs().size());
        System.out.println("FAILED: " + storage.getFailedJobs().size());

        // RUNNING
        System.out.println("---- RUNNING JOBS ----");

        for (Long pid : storage.getRunningJobs().keySet()) {
            Job job = storage.getRunningJobs().get(pid);

            System.out.println(
                job.getId() +
                " | PID=" + pid +
                " | CPU=" + job.getCpuCores() +
                " | MEM=" + job.getMemMb() +
                "MB | PROG=" + (int)(job.getProgress() * 100) + "%"
            );
        }
        System.out.println("===========================\n");
    }
}