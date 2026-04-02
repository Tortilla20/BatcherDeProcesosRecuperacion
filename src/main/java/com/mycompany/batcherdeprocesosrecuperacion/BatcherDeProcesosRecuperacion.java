/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.batcherdeprocesosrecuperacion;

import com.mycompany.batcherdeprocesosrecuperacion.monitor.Monitor;
import com.mycompany.batcherdeprocesosrecuperacion.resource.ResourceManager;
import com.mycompany.batcherdeprocesosrecuperacion.scheduler.Scheduler;
import com.mycompany.batcherdeprocesosrecuperacion.storage.JobStorage;
import com.mycompany.batcherdeprocesosrecuperacion.loader.JobLoader;

/**
 *
 * @author duroi
 */
public class BatcherDeProcesosRecuperacion {
    public static void main(String[] args) {

        // Crear recursos
        JobStorage storage = new JobStorage();
        ResourceManager rm = new ResourceManager(4, 2048); // 4 cores, 2048 mb ram

        // Cargar JOBS desde YAML
        JobLoader loader = new JobLoader();
        loader.loadJobs("jobs", storage);

        // Admitir JOBS segun recursos los recursos disponibles
        rm.admitJobs(storage);

        // FCFS o RR
        Scheduler scheduler = new Scheduler(rm, "FCFS", 200);
        scheduler.schedule(storage);

        // Lanzar monitor
        Monitor monitor = new Monitor(rm);
        monitor.start(storage);

        while (!storage.getRunningJobs().isEmpty() || !storage.getReadyJobs().isEmpty() || !storage.getWaitingJobs().isEmpty()) {
            try {
                Thread.sleep(500); // actualizar cada medio segundo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Todos los jobs han terminado.");
    }
}