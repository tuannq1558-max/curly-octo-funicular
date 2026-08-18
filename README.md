# SP26SE025 - System for Retinal Vascular Health Screening

## Project Information

English:
System for Retinal Vascular Health Screening

Vietnamese:
Hệ Thống Sàng Lọc Sức Khỏe Mạch Máu Võng Mạc

Abbreviation:
SP26SE025

## Overview

SP26SE025 is an AI-assisted retinal vascular health screening system
designed to support early detection of potential systemic health risks
through retinal image analysis.

The system analyzes retinal fundus and OCT images and provides
risk assessments and visual explanations to support healthcare
professionals in clinical decision-making.

The system is a Clinical Decision Support (CDS) tool.
It assists healthcare professionals and does not replace
professional medical diagnosis.

## Main Objectives

- Support early screening of potential vascular health risks.
- Analyze retinal fundus and OCT images using AI.
- Provide explainable AI results through annotations and heatmaps.
- Support doctors in reviewing and validating AI findings.
- Expand access to non-invasive preventive screening.
- Maintain secure and privacy-aware medical data management.
- Support multiple clinics and large numbers of users.

## System Components

### 1. Java Backend

Technology:

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Maven
- Docker

Responsibilities:

- Authentication and authorization
- User management
- Doctor and clinic management
- Retinal image management
- AI analysis management
- Medical records
- Reports
- Notifications
- Consultation
- Payment and service packages
- Audit logging

### 2. AI Core

Technology:

- Python
- Machine Learning / Deep Learning
- REST API

Responsibilities:

- Retinal image preprocessing
- Retinal vessel analysis
- Risk assessment
- Abnormality detection
- Heatmap generation
- Explainable AI output

### 3. Web Application

Technology:

- React
- TypeScript

The web application provides interfaces for:

- Patients
- Doctors
- Clinics
- Administrators

## User Roles

### Patient

- Register and login
- Upload retinal images
- View analysis results
- View risk levels
- View annotated images
- View analysis history
- Download reports
- Manage medical information
- Communicate with doctors
- Purchase analysis packages

### Doctor

- Manage assigned patients
- Review AI results
- Validate AI findings
- Add medical notes
- View patient history
- Search and filter patients
- Provide AI feedback
- Communicate with patients
- View statistics

### Clinic

- Manage doctors and patients
- Upload multiple retinal images
- Monitor analysis results
- Generate screening reports
- Monitor package usage
- Receive high-risk alerts
- Export statistics

### Administrator

- Manage users and clinics
- Manage roles and permissions
- Configure AI parameters
- Manage service packages
- Monitor system statistics
- Manage audit logs
- Manage privacy settings
- Manage notifications

## Non-Functional Requirements

The system is designed with the following requirements:

- AI analysis target: 10–20 seconds per image
- Bulk processing: at least 100 images per batch
- Dashboard response target: less than 3 seconds
- System availability: at least 99%
- Horizontal AI service scalability
- TLS 1.2+ for data transmission
- AES-256 encryption for stored sensitive data
- Role-Based Access Control (RBAC)
- Patient data anonymization
- Centralized logging and auditing
- Explainable AI results
- AI model and threshold version tracking

## Architecture

```text
                   ┌──────────────────┐
                   │   React Web App  │
                   └────────┬─────────┘
                            │
                         REST API
                            │
                   ┌────────▼─────────┐
                   │  Java Spring     │
                   │     Backend      │
                   └──────┬─────┬──────┘
                          │     │
              ┌───────────┘     └────────────┐
              ▼                              ▼
       ┌─────────────┐                ┌─────────────┐
       │ PostgreSQL  │                │  AI Core    │
       │  Database   │                │   Python    │
       └─────────────┘                └──────┬──────┘
                                             │
                                             ▼
                                      Retinal Analysis
