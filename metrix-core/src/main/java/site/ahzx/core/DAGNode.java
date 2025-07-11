package site.ahzx.core;

import site.ahzx.domain.entity.TaskDef;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DAGNode  {
    String nodeId;
    TaskDef task;
    List<String> dependencies = new ArrayList<>(); // 上游节点ID
}
