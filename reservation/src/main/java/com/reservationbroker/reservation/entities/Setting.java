package com.reservationbroker.reservation.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Data
@Table(name = "settings")
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "hours_before_first_msg", nullable = false)
    private int hoursBeforeFirstMsg;

    @Column(name = "hours_before_second_msg")
    private Integer hoursBeforeSecondMsg;

    @Column(name = "number_of_messages", nullable = false)
    private int numberOfMessages;

    @Column(name = "message_template", nullable = false)
    private String messageTemplate;

    @Column(name = "days_to_keep", nullable = false)
    private int daysToKeep;

    @Column(name = "cenovnik", nullable = false)
    private boolean cenovnik;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    // New fields for logo, background image, etc.
    private String imageUrlLogo;
    private String imageUrlBackground;

    @Enumerated(EnumType.STRING)
    private ButtonShape buttonShape;

    @Enumerated(EnumType.STRING)
    private Theme theme;

    // JSON field for storing up to 10 URLs
    @Column(name = "image_url_10", columnDefinition = "longtext")
    private String imageUrl10Json; // Store JSON string

    private Boolean prikaziCene;  // New field for controlling price display

    // Additional fields for contact info
    private String email;
    private String phone;
    private String address;
    private String city;
    private String zip;

    @Enumerated(EnumType.STRING)
    @Column(name = "timezone", nullable = false)
    private Timezone timezone;  // New field for timezone selection

    public enum ButtonShape {
        PILL, ROUNDED, RECTANGLE
    }

    public enum Theme {
        LIGHT, DARK
    }

    // Enum for Timezone with cities
    public enum Timezone {
        UTC_MINUS_12("UTC-12:00 (Baker Island)"),
        UTC_MINUS_11("UTC-11:00 (American Samoa)"),
        UTC_MINUS_10("UTC-10:00 (Hawaii)"),
        UTC_MINUS_9("UTC-09:00 (Alaska)"),
        UTC_MINUS_8("UTC-08:00 (Los Angeles)"),
        UTC_MINUS_7("UTC-07:00 (Denver)"),
        UTC_MINUS_6("UTC-06:00 (Mexico City)"),
        UTC_MINUS_5("UTC-05:00 (New York)"),
        UTC_MINUS_4("UTC-04:00 (Santiago)"),
        UTC_MINUS_3("UTC-03:00 (Buenos Aires)"),
        UTC_MINUS_2("UTC-02:00 (Mid-Atlantic)"),
        UTC_MINUS_1("UTC-01:00 (Azores)"),
        UTC_0("UTC+00:00 (London)"),
        UTC_PLUS_1("UTC+01:00 (Berlin)"),
        UTC_PLUS_2("UTC+02:00 (Cairo)"),
        UTC_PLUS_3("UTC+03:00 (Moscow)"),
        UTC_PLUS_4("UTC+04:00 (Dubai)"),
        UTC_PLUS_5("UTC+05:00 (Tashkent)"),
        UTC_PLUS_5_30("UTC+05:30 (New Delhi)"),
        UTC_PLUS_6("UTC+06:00 (Dhaka)"),
        UTC_PLUS_7("UTC+07:00 (Bangkok)"),
        UTC_PLUS_8("UTC+08:00 (Beijing)"),
        UTC_PLUS_9("UTC+09:00 (Tokyo)"),
        UTC_PLUS_10("UTC+10:00 (Sydney)"),
        UTC_PLUS_11("UTC+11:00 (Solomon Islands)"),
        UTC_PLUS_12("UTC+12:00 (Fiji)");

        private final String displayName;

        Timezone(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @JsonIgnore
    public List<String> getImageUrl10() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (imageUrl10Json != null && !imageUrl10Json.isEmpty()) {
                return objectMapper.readValue(imageUrl10Json, List.class);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle exception as needed
        }
        return new ArrayList<>();
    }

    @JsonIgnore
    public void setImageUrl10(List<String> imageUrl10) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.imageUrl10Json = objectMapper.writeValueAsString(imageUrl10);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle exception as needed
        }
    }

    // Method to add a new image URL if less than 10 exist
    public boolean addImageUrl(String imageUrl) {
        List<String> imageUrlList = getImageUrl10();
        if (imageUrlList.size() < 10) {
            imageUrlList.add(imageUrl);
            setImageUrl10(imageUrlList);
            return true;
        } else {
            return false;  // Cannot add more than 10 URLs
        }
    }

    // Method to get the current number of images
    public int getNumberOfImages() {
        return getImageUrl10().size();
    }
}
