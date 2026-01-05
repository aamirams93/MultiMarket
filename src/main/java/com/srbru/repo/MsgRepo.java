package com.srbru.repo;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.srbru.entity.MsgEntity;

@RepositoryRestResource(exported = false)
public interface MsgRepo extends JpaRepository<MsgEntity, Serializable>
{

	MsgEntity findByWindowIdAndEventId(String windowId, String eventId);
}
