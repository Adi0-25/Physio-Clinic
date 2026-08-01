package com.drsanjitclinic.config;

import com.drsanjitclinic.model.Disease;
import com.drsanjitclinic.repository.DiseaseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds the physiotherapy conditions catalogue the first time the application
 * starts against an empty database. Safe to run on every restart because it
 * checks count() == 0 first.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final DiseaseRepository diseaseRepository;

    public DataSeeder(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @Override
    public void run(String... args) {
        if (diseaseRepository.count() > 0) {
            return;
        }

        List<Disease> diseases = List.of(
            new Disease(null, "Lower Back Pain",
                "Chronic or acute pain in the lumbar spine caused by poor posture, muscle strain, or disc issues.",
                "Manual therapy, core strengthening exercises, posture correction, IFT/TENS electrotherapy, ergonomic guidance.",
                14, 30, 3, 500.0, 350.0, "Moderate"),

            new Disease(null, "Cervical Spondylosis (Neck Pain)",
                "Degeneration of neck vertebrae and discs leading to stiffness, pain, and sometimes arm numbness.",
                "Cervical traction, neck mobilization, isometric strengthening, heat therapy, posture retraining.",
                21, 45, 3, 500.0, 350.0, "Moderate"),

            new Disease(null, "Frozen Shoulder (Adhesive Capsulitis)",
                "Stiffness and pain in the shoulder joint that restricts range of motion.",
                "Capsular stretching, mobilization techniques, ultrasound therapy, progressive range-of-motion exercises.",
                60, 120, 4, 500.0, 400.0, "Severe"),

            new Disease(null, "Knee Osteoarthritis",
                "Wear and tear of knee cartilage causing pain, swelling, and reduced mobility, common in older adults.",
                "Quadriceps strengthening, low-impact exercises, hot/cold therapy, gait training, weight management advice.",
                30, 90, 3, 500.0, 400.0, "Moderate"),

            new Disease(null, "Sciatica",
                "Pain radiating along the sciatic nerve from lower back to the leg, often due to disc compression.",
                "Nerve mobilization, McKenzie exercises, traction, core stability training, posture correction.",
                21, 60, 3, 500.0, 400.0, "Moderate"),

            new Disease(null, "Slip Disc (Prolapsed Intervertebral Disc)",
                "Displacement of spinal disc material that can compress nearby nerves and cause severe back or leg pain.",
                "Spinal decompression therapy, core strengthening, manual therapy, activity modification, home exercise plan.",
                30, 90, 4, 600.0, 450.0, "Severe"),

            new Disease(null, "Tennis Elbow (Lateral Epicondylitis)",
                "Overuse injury causing pain on the outer elbow from repetitive wrist and arm motion.",
                "Eccentric strengthening exercises, deep tissue massage, ultrasound therapy, bracing advice.",
                14, 42, 3, 500.0, 350.0, "Mild"),

            new Disease(null, "Ankle Sprain",
                "Stretching or tearing of ankle ligaments, usually from twisting injuries during sports or a misstep.",
                "RICE protocol guidance, balance and proprioception training, progressive strengthening, taping techniques.",
                7, 21, 3, 400.0, 300.0, "Mild"),

            new Disease(null, "Post-Surgery Rehabilitation",
                "Recovery program after orthopedic surgeries such as joint replacement, ligament repair, or fracture fixation.",
                "Customized rehab protocol, gradual mobility restoration, strength building, functional training.",
                30, 120, 4, 600.0, 450.0, "Severe"),

            new Disease(null, "Sports Injury Rehabilitation",
                "Muscle strains, ligament tears, and joint injuries sustained during sports or physical activity.",
                "Sport-specific rehabilitation, strength and conditioning, taping/bracing, return-to-play testing.",
                14, 60, 4, 550.0, 400.0, "Moderate"),

            new Disease(null, "Stroke Rehabilitation",
                "Neurological rehabilitation to help regain movement, balance, and independence after a stroke.",
                "Neuro-developmental therapy, gait training, balance exercises, functional retraining, family education.",
                60, 180, 5, 700.0, 500.0, "Severe"),

            new Disease(null, "Paralysis / Nerve Injury Rehabilitation",
                "Loss of muscle function due to nerve damage or spinal cord injury requiring long-term therapy.",
                "Electrical muscle stimulation, passive/active exercises, splinting advice, functional training, home visits available.",
                90, 270, 5, 700.0, 500.0, "Severe")
        );

        diseaseRepository.saveAll(diseases);
    }
}
