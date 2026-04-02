<h1 align="center">Batcher de Procesos Recuperación</h1>
<p align="center">Iván Duro Fernández</p>

<p align="center">Simulador de un batcher de procesos en Java, que permite cargar jobs desde YAML, planificarlos y ejecutarlos simulando el uso de recursos del sistema.</p>

## Funcionalidad principal

1. **Carga de jobs**  
   - Lee archivos YAML desde la carpeta `jobs`.  
   - Cada job define: `id`, `name`, `priority`, recursos (`cpu_cores`, `memory`) y duración (`duration_ms`).  
   - Los jobs se almacenan en colas según su estado: `NEW`, `READY`, `WAITING`.

2. **Gestión de recursos**  
   - Controla cores de CPU y memoria disponible.  
   - Los jobs solo pasan a `READY` si hay recursos suficientes; si no, van a `WAITING`.

3. **Planificación de jobs**  
   - **FCFS (First Come, First Served):** ejecuta los jobs por orden de llegada a `READY`.  
   - **Round Robin (RR):** reparte la CPU por turnos según un quantum configurable.  

4. **Ejecución de procesos**  
   - Cada job `RUNNING` se lanza como un proceso hijo (`WorkerMain`).  
   - Los workers simulan la ejecución con `Thread.sleep()` y envían progresos periódicos por stdout.  
   - Al finalizar, los jobs pasan a `DONE` o `FAILED`, liberando recursos para nuevos jobs.

5. **Monitor en tiempo real**  
   - Imprime el estado de los recursos y colas: `READY`, `WAITING`, `RUNNING`, `DONE`, `FAILED`.  
   - Muestra detalles de los jobs en ejecución: PID, prioridad, cores, memoria y progreso.

## Estructura de paquetes
com.mycompany.batcherdeprocesosrecuperacion
   - main -> BatcherDeProcesosRecuperacion.java
   - model -> Job.java, YamlJob.java
   - loader -> JobLoader.java
   - storage -> JobStorage.java
   - scheduler -> Scheduler.java
   - resources -> ResourceManager.java
   - process -> ProcessCreator.java, WorkerMain.java
   - monitor -> Monitor.java

## Ejecución

1. Coloca los YAML de tus jobs en la carpeta `jobs`.  
2. Ejecuta el proyecto desde NetBeans o Maven.  
3. Observar en consola cómo los jobs pasan por los estados y cómo evoluciona su progreso en tiempo real.
