package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.Session;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SessionService {
    Session addSession(Session session, String userId) throws ObjectNotFoundException;

    Session updateSession(Session session, String userId) throws ObjectNotFoundException;

    Session getSession(String sessionId, String userId) throws ObjectNotFoundException;

    void deleteSession(String sessionId, String userId) throws ObjectNotFoundException;

    List<Session> getAllSessionsByUser(String userId) throws ObjectNotFoundException;

}
