/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.batcherdeprocesosrecuperacion.process;

import com.mycompany.batcherdeprocesosrecuperacion.model.Job;
import java.io.File;

/**
 *
 * @author duroi
 */
/**
 * Clase que crea procesos con ProcessBuilder
 */
public class ProcessCreator {

    public static Process createProcess(Job job) throws Exception {

        String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");

        ProcessBuilder pb = new ProcessBuilder(
                "java",
                "-cp",
                "target/classes",
                "com.mycompany.batcherdeprocesosrecuperacion.worker.WorkerMain",
                job.getId(),
                String.valueOf(job.getDurationMs())
        );
        return pb.start();
    }
}