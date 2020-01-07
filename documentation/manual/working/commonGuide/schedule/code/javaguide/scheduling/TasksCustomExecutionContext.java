/*
 * Copyright (C) Lightbend Inc. <https://www.lightbend.com>
 */

// ###replace: package tasks;
package javaguide.scheduling;

// #custom-task-execution-context
import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class TasksCustomExecutionContext extends CustomExecutionContext {

  @Inject
  public TasksCustomExecutionContext(ActorSystem actorSystem) {
    super(actorSystem, "tasks-dispatcher");
  }
}
// #custom-task-execution-context

// #task-using-custom-execution-context
// ###replace: public class SomeTask
class SomeTask {

  private final ActorSystem actorSystem;
  private final TasksCustomExecutionContext executor;

  @Inject
  public SomeTask(ActorSystem actorSystem, TasksCustomExecutionContext executor) {
    this.actorSystem = actorSystem;
    this.executor = executor;

    this.initialize();
  }

  private void initialize() {
    this.actorSystem
        .scheduler()
        .scheduleAtFixedRate(
            Duration.create(10, TimeUnit.SECONDS), // initialDelay
            Duration.create(1, TimeUnit.MINUTES), // interval
            () -> actorSystem.log().info("Running block of code"),
            this.executor // using the custom executor
            );
  }
}
// #task-using-custom-execution-context
