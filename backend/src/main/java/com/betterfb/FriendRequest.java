/**
 * Entity class representing a friend request in the system.
 */
package com.betterfb;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * FriendRequest entity class
 */
@Entity
public class FriendRequest {

    /**
     * Unique identifier of the friend request.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User who sent the friend request.
     */
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    /**
     * User who received the friend request.
     */
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    /**
     * Date and time when the friend request was sent.
     */
    private LocalDateTime sentTime;

    /**
     * Whether the friend request has been accepted or not.
     */
    private boolean accepted;

    /**
     * Gets the unique identifier of the friend request.
     *
     * @return the friend request's id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the friend request.
     *
     * @param id the friend request's id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the user who sent the friend request.
     *
     * @return the user who sent the friend request
     */
    public User getSender() {
        return sender;
    }

    /**
     * Sets the user who sent the friend request.
     *
     * @param sender the user who sent the friend request to set
     */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /**
     * Gets the user who received the friend request.
     *
     * @return the user who received the friend request
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     * Sets the user who received the friend request.
     *
     * @param receiver the user who received the friend request to set
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    /**
     * Gets the date and time when the friend request was sent.
     *
     * @return the date and time when the friend request was sent
     */
    public LocalDateTime getSentTime() {
        return sentTime;
    }

    /**
     * Sets the date and time when the friend request was sent.
     *
     * @param sentTime the date and time when the friend request was sent to set
     */
    public void setSentTime(LocalDateTime sentTime) {
        this.sentTime = sentTime;
    }

    /**
     * Gets whether the friend request has been accepted or not.
     *
     * @return true if the friend request has been accepted, false otherwise
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * Sets whether the friend request has been accepted or not.
     *
     * @param accepted whether the friend request has been accepted or not to set
     */
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}

