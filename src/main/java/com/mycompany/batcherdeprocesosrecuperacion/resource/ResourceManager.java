/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.batcherdeprocesosrecuperacion.resource;

import com.mycompany.batcherdeprocesosrecuperacion.storage.JobStorage;
import com.mycompany.batcherdeprocesosrecuperacion.model.Job;
import java.util.Iterator;

/**
 *
 * @author duroi
 */

// Clase que se encarga de gestionar la CPU y la memoria
public class ResourceManager {

    private int totalCores;
    private int availableCores;
    private int totalMemMb;
    private int availableMemMb;

    public ResourceManager(int cores, int memMb) {
        this.totalCores = cores;
        this.availableCores = cores;
        this.totalMemMb = memMb;
        this.availableMemMb = memMb;
    }
    
    public int getAvailableCores() {
        return availableCores;
    }

    public int getAvailableMemMb() {
        return availableMemMb;
    }

    public int getTotalCores() {
        return totalCores;
    }

    public int getTotalMemMb() {
        return totalMemMb;
    }

    // Admite jobs desde NEW, READY o WAITING
    public void admitJobs(JobStorage storage) {
        Iterator<Job> it = storage.getNewJobs().iterator();

        while (it.hasNext()) {
            Job job = it.next();
            if (canRun(job)) {
                // Si hay recursos, se pone a READY
                reserveResources(job);
                job.setState(Job.State.READY);
                storage.getReadyJobs().add(job);
                System.out.println("Job pasa a READY: " + job.getId());
            } else {
                // Si no hay recursos, se pone a WAITING
                job.setState(Job.State.WAITING);
                storage.getWaitingJobs().add(job);
                System.out.println("Job pasa a WAITING: " + job.getId());
            }
            // (IMPORTANTE) quitar de NEW
            it.remove();
        }
    }
    
    // Revisar si ahora los JOBS en WAITING pueden pasar a READY
    public void checkWaitingJobs(JobStorage storage) {
        Iterator<Job> it = storage.getWaitingJobs().iterator();

        while (it.hasNext()) {
            Job job = it.next();
            if (canRun(job)) {
                reserveResources(job);
                job.setState(Job.State.READY);
                storage.getReadyJobs().add(job);
                System.out.println("WAITING -> READY: " + job.getId());
                it.remove();
            }
        }
    }

    // Comprueba si hay recursos suficientes
    private boolean canRun(Job job) {
        return job.getCpuCores() <= availableCores && job.getMemMb() <= availableMemMb;
    }

    // Reservar recursos cuando un job entra en READY/RUNNING
    private void reserveResources(Job job) {
        availableCores -= job.getCpuCores();
        availableMemMb -= job.getMemMb();
    }

    // Liberar recursos cuando un job termina
    public void releaseResources(Job job) {
        availableCores += job.getCpuCores();
        availableMemMb += job.getMemMb();
    }
}