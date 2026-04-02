/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.batcherdeprocesosrecuperacion.scheduler;

import com.mycompany.batcherdeprocesosrecuperacion.process.ProcessCreator;
import com.mycompany.batcherdeprocesosrecuperacion.process.ProcessCreator;
import com.mycompany.batcherdeprocesosrecuperacion.resource.ResourceManager;
import com.mycompany.batcherdeprocesosrecuperacion.resource.ResourceManager;
import com.mycompany.batcherdeprocesosrecuperacion.storage.JobStorage;
import com.mycompany.batcherdeprocesosrecuperacion.model.Job;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author duroi
 */
// Clase que se encarga de decidir que JOBS pasan de READY a RUNNING
public class Scheduler {

    private ResourceManager rm;
    private String policy; // FCFS o RR
    private long quantum; // solo RR

    public Scheduler(ResourceManager rm, String policy, long quantum) {
        this.rm = rm;
        this.policy = policy;
        this.quantum = quantum;
    }

    public void schedule(JobStorage storage) {
        if (policy.equalsIgnoreCase("FCFS")) {
            runFCFS(storage);
        } else if (policy.equalsIgnoreCase("RR")) {
            runRR(storage);
        }
    }

    // (FCFS) coger en orden y pasar a RUNNING
    private void runFCFS(JobStorage storage) {

        while (!storage.getReadyJobs().isEmpty()) {
            Job job = storage.getReadyJobs().poll();

            try {
                // Crear proceso
                Process p = ProcessCreator.createProcess(job);
                long pid = p.pid();
                job.setState(Job.State.RUNNING);
                job.setStartTime(java.time.Instant.now());
                storage.getRunningJobs().put(pid, job);
                System.out.println("RUNNING: " + job.getId() + " PID =" + pid);
                // Leer salida del proceso
                new Thread(() -> {
                    try (java.io.BufferedReader reader = new java.io.BufferedReader(new InputStreamReader(p.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("Progreso -> " + line);
                            String[] partes = line.split(":");
                            if (partes.length == 2) {
                                double prog = Double.parseDouble(partes[1]);
                                job.setProgress(prog);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();

                // Esperar a que termine
                new Thread(() -> {
                    try {
                        int exitCode = p.waitFor();
                        job.setEndTime(java.time.Instant.now());
                        // Liberar recursos
                        rm.releaseResources(job);
                        storage.getRunningJobs().remove(pid);
                        if (exitCode == 0) {
                            job.setState(Job.State.DONE);
                            storage.getDoneJobs().add(job);
                            System.out.println("DONE: " + job.getId());
                        } else {
                            job.setState(Job.State.FAILED);
                            storage.getFailedJobs().add(job);
                            System.out.println("FAILED: " + job.getId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Round Robin
    private void runRR(JobStorage storage) {

        Queue<Job> queue = new LinkedList<>(storage.getReadyJobs());
        storage.getReadyJobs().clear();
        while (!queue.isEmpty()) {
            Job job = queue.poll();
            if (canRun(job)) {
                job.setState(Job.State.RUNNING);
                job.setStartTime(Instant.now());
                storage.getRunningJobs().put(System.nanoTime(), job);
                System.out.println("RUNNING (RR): " + job.getId());
                queue.add(job);
                break; // al aser un bucle infinito, salir con break
            } else {
                queue.add(job);
            }
        }
    }

    // Comprobar si puede ejecutarse con recursos actuales
    private boolean canRun(Job job) {
        return job.getCpuCores() <= rm.getAvailableCores() && job.getMemMb() <= rm.getAvailableMemMb();
    }
}