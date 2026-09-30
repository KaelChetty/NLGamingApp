# Next Level Gaming & Esports Arena

## XHAW5112w - Task 2 Group Project

This repository contains the Android mobile application developed for the Next Level Gaming & Esports Arena project as part of XHAW5112w Work Integrated Learning.

The application was developed in Android Studio using Kotlin and XML. The purpose of the application is to give users an easy way to explore the different gaming experiences offered by the arena, calculate estimated prices, complete a booking request and contact the arena.

## Developer Contribution

The Android application contained in this repository was developed and implemented by **Kael Chetty** as my contribution towards the group project.

My work included converting the planned high-fidelity designs into a functional Android application, creating the different application screens, implementing navigation, developing the price calculator, creating the booking process, adding form validation and testing the completed application.

The overall project was completed as a group project, with group members contributing towards the planning and high-fidelity wireframes.

## Main Features

The application includes:

- Home page and application navigation
- About Us page
- Experiences overview
- Individual pages for each gaming experience
- Price calculator
- Quantity-based discounts
- 15% VAT calculation
- Booking form
- Booking validation
- Booking summary
- Booking confirmation and reference number
- Contact form
- Local storage of booking and contact information

## Experiences and Prices

- Ultimate Gamer Pass - R1 500
- VIP Gaming Experience - R1 500
- Esports Training Package - R1 500
- Birthday Party Package - R1 500
- Virtual Reality Experience - R750
- Racing Simulator Challenge - R750
- Escape Room Challenge - R750

## Quantity Discounts

The application calculates discounts according to the selected quantity:

- 1 booking - 0% discount
- 2 bookings - 5% discount
- 3 bookings - 10% discount
- 4 or more bookings - 15% discount

VAT of 15% is calculated after the applicable discount has been applied.

## Development Challenges

During development, I experienced several challenges while building and testing the application.

One of the main challenges was testing the application using the Android Studio emulator because the development computer had limited available system resources. This caused the emulator to become slow or freeze. To solve this problem, I built the APK and tested the application directly on a physical Android device.

Another challenge was maintaining navigation and functionality across the different application screens. The application contains multiple layouts, so I had to make sure that buttons, forms and navigation worked correctly between the different pages.

The booking and calculator features also required careful testing to make sure that quantities, discounts and VAT were calculated correctly and that the required booking information was validated before continuing.

## Testing

The application was manually tested to confirm that:

- Navigation between screens works correctly
- The Android Back button works correctly
- Experience pages display correctly
- Price calculations work correctly
- Quantity discounts are applied correctly
- VAT is calculated after the discount
- Booking validation works
- Booking summaries display correctly
- Booking confirmation references are generated
- Contact form validation works
- Booking and contact information can be stored locally

## Technologies Used

- Android Studio
- Kotlin
- XML
- SharedPreferences
- Git
- GitHub

## Images and Visual Assets

Visual assets used within the application were generated using **OpenAI ChatGPT image generation** for the purpose of the project. These images were used to support the visual design of the gaming experiences and application interface.

The generated images were incorporated into the Android application as drawable resources.

## Data Storage

The application uses local storage through SharedPreferences for demonstration purposes.

Booking and contact information is stored locally on the device. The application does not currently connect to a live online booking system, external database or email service.

## Author

**Kael Chetty**

XHAW5112w - Work Integrated Learning  
Next Level Gaming & Esports Arena Group Project
