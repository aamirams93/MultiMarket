package com.srbru.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "TBL_APP_MESSAGES")
@Data
public class MsgEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MESSAGE_ID")
	private Integer msgId;

	@Column(name = "WINDOW_ID")
	private String windowId;

	@Column(name = "MESSAGE")
	private String message;

	@Column(name = "EVENT_ID")
	private String eventId;

}
