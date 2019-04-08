package ua.org.petroff.game.engine.entities;

import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;

public class QueueEntityDescriber {

    private QueueDrawInterface view;

    private EntityInterface entity;

    public QueueEntityDescriber(QueueDrawInterface view, EntityInterface entity) {
        this.view = view;
        this.entity = entity;
    }

}
