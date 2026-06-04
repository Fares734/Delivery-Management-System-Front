# Delivery Management System - Android Application

## Overview

This Android application allows delivery agents and controllers to manage and monitor delivery operations in real time.

The application was developed using Android Studio and follows a client-server architecture connected to a Spring Boot backend.

## Features

### Delivery Agent

- Secure login
- Daily delivery list
- Delivery details
- Mark delivery as completed
- Cancel delivery with remarks
- Synchronization with server
- Receive controller messages

### Controller

- Dashboard with KPIs
- Daily delivery monitoring
- Delivery search by period and delivery agent
- Delivery statistics
- View cancellation remarks
- Send information and urgent messages

## Technologies

- Java
- Android Studio
- Retrofit
- Room Database
- RecyclerView
- Material Design
- JWT Authentication

## Architecture

```text
Android App
      |
      | Retrofit
      |
Spring Boot REST API
      |
      | JPA/Hibernate
      |
MySQL Database
