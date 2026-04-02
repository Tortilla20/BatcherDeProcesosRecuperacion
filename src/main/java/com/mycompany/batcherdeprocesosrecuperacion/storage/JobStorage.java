/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.batcherdeprocesosrecuperacion.storage;

import com.mycompany.batcherdeprocesosrecuperacion.model.Job;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author duroi
 */

// Clase que guarda todas las colas del sistema
public class JobStorage {

    // Cola de jobs recien cargados
    private Queue<Job> newJobs = new LinkedList<>();

    // Cola listos para ejecutar
    private Queue<Job> readyJobs = new LinkedList<>();

    // Cola esperando recursos
    private Queue<Job> waitingJobs = new LinkedList<>();

    // Jobs en ejecución
    private HashMap<Long, Job> runningJobs = new HashMap<>();

    // Jobs terminados correctamente
    private Queue<Job> doneJobs = new LinkedList<>();

    // Jobs que fallaron
    private Queue<Job> failedJobs = new LinkedList<>();

    public Queue<Job> getNewJobs() {
        return newJobs;
    }

    public Queue<Job> getReadyJobs() {
        return readyJobs;
    }

    public Queue<Job> getWaitingJobs() {
        return waitingJobs;
    }

    public HashMap<Long, Job> getRunningJobs() {
        return runningJobs;
    }

    public Queue<Job> getDoneJobs() {
        return doneJobs;
    }

    public Queue<Job> getFailedJobs() {
        return failedJobs;
    }
}