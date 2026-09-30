package com.example.nlgamingapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private var currentScreen = R.layout.activity_main
    private val history = mutableListOf<Int>()

    private val names = listOf(
        "Ultimate Gamer Pass",
        "VIP Gaming Experience",
        "Esports Training Package",
        "Birthday Party Package",
        "Virtual Reality Experience",
        "Racing Simulator Challenge",
        "Escape Room Challenge"
    )

    private val prices = listOf(
        1500.0, 1500.0, 1500.0, 1500.0,
        750.0, 750.0, 750.0
    )

    private var chosenIndex = 0
    private var quantityDraft = "1"

    // Calculator data
    private var experience = ""
    private var unitPrice = 0.0
    private var quantity = 0
    private var subtotal = 0.0
    private var discountRate = 0
    private var discount = 0.0
    private var discounted = 0.0
    private var vat = 0.0
    private var total = 0.0
    private var calculated = false

    // Booking data
    private var name = ""
    private var email = ""
    private var phone = ""
    private var date = ""
    private var time = ""
    private var requests = ""
    private var reference = ""
    private var submitted = false

    // Contact form data
    private var contactName = ""
    private var contactEmail = ""
    private var contactPhone = ""
    private var contactSubject = ""
    private var contactMessage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        show(R.layout.activity_main, false)

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() = back()
            }
        )
    }

    // Display the selected screen
    private fun show(layout: Int, remember: Boolean = true) {
        saveVisibleDraft()

        if (remember && layout != currentScreen) {
            history.add(currentScreen)
        }

        currentScreen = layout
        setContentView(layout)
        setupMenu()

        when (layout) {
            R.layout.activity_main -> home()

            R.layout.activity_about -> {
                link(R.id.btnAboutBookNow, R.layout.activity_calculate)
                link(R.id.footerAboutContact, R.layout.activity_contact)
            }

            R.layout.activity_experiences -> experiences()

            R.layout.activity_vr -> detail(
                R.id.btnVrBookNow,
                R.id.btnVrBack,
                R.id.footerVrContact
            )

            R.layout.activity_racing -> detail(
                R.id.btnRacingBookNow,
                R.id.btnRacingBack,
                R.id.footerRacingContact
            )

            R.layout.activity_escape -> detail(
                R.id.btnEscapeBookNow,
                R.id.btnEscapeBack,
                R.id.footerEscapeContact
            )

            R.layout.activity_ultimate -> detail(
                R.id.btnUltimateBookNow,
                R.id.btnUltimateBack,
                R.id.footerUltimateContact
            )

            R.layout.activity_vip -> detail(
                R.id.btnVipBookNow,
                R.id.btnVipBack,
                R.id.footerVipContact
            )

            R.layout.activity_esports -> detail(
                R.id.btnEsportsBookNow,
                R.id.btnEsportsBack,
                R.id.footerEsportsContact
            )

            R.layout.activity_birthday -> detail(
                R.id.btnBirthdayBookNow,
                R.id.btnBirthdayBack,
                R.id.footerBirthdayContact
            )

            R.layout.activity_calculate -> calculator()
            R.layout.activity_booking -> booking()
            R.layout.activity_summary -> summary()
            R.layout.activity_confirmation -> confirmation()
            R.layout.activity_contact -> contact()
        }
    }

    private fun link(id: Int, destination: Int) {
        findViewById<View>(id).setOnClickListener {
            show(destination)
        }
    }

    // Homepage navigation
    private fun home() {
        link(R.id.btnBookNow, R.layout.activity_calculate)
        link(R.id.btnVrDetails, R.layout.activity_vr)
        link(R.id.btnRacingDetails, R.layout.activity_racing)
        link(R.id.btnMoreExperiences, R.layout.activity_experiences)
        link(R.id.footerContact, R.layout.activity_contact)
    }

    // Experiences overview navigation
    private fun experiences() {
        link(R.id.btnUltimateDetails, R.layout.activity_ultimate)
        link(R.id.btnVipDetails, R.layout.activity_vip)
        link(R.id.btnEsportsDetails, R.layout.activity_esports)
        link(R.id.btnBirthdayDetails, R.layout.activity_birthday)
        link(R.id.btnOverviewVrDetails, R.layout.activity_vr)
        link(R.id.btnOverviewRacingDetails, R.layout.activity_racing)
        link(R.id.btnEscapeDetails, R.layout.activity_escape)
        link(R.id.btnExperiencesBookNow, R.layout.activity_calculate)
        link(R.id.footerExperiencesContact, R.layout.activity_contact)
    }

    // Shared navigation for individual experience pages
    private fun detail(bookId: Int, backId: Int, contactId: Int) {
        link(bookId, R.layout.activity_calculate)

        findViewById<View>(backId).setOnClickListener {
            back()
        }

        link(contactId, R.layout.activity_contact)
    }

    // Helper functions
    private fun money(value: Double): String {
        return "R" + String.format(Locale.US, "%,.2f", value)
    }

    private fun setText(id: Int, value: String) {
        findViewById<TextView>(id).text = value
    }

    private fun field(id: Int): EditText {
        return findViewById(id)
    }

    private fun value(id: Int): String {
        return field(id).text.toString().trim()
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    // FEE CALCULATOR
    private fun calculator() {
        val spinner = findViewById<Spinner>(R.id.spinnerExperience)
        val count = field(R.id.edtQuantity)
        val results = findViewById<View>(R.id.layoutCalculationResults)
        val next = findViewById<View>(R.id.btnContinueBooking)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            names
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinner.adapter = adapter
        spinner.setSelection(chosenIndex)
        count.setText(quantityDraft)

        fun render() {
            setText(
                R.id.txtSubtotal,
                "Subtotal: ${money(subtotal)}"
            )

            setText(
                R.id.txtDiscount,
                "Discount ($discountRate%): -${money(discount)}"
            )

            setText(
                R.id.txtAfterDiscount,
                "After Discount: ${money(discounted)}"
            )

            setText(
                R.id.txtVat,
                "VAT (15%): ${money(vat)}"
            )

            setText(
                R.id.txtGrandTotal,
                "TOTAL: ${money(total)}"
            )

            results.visibility =
                if (calculated) View.VISIBLE else View.GONE

            next.visibility =
                if (calculated) View.VISIBLE else View.GONE
        }

        render()

        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    setText(
                        R.id.txtSelectedPrice,
                        "Experience price: ${money(prices[position])}"
                    )

                    if (position != chosenIndex) {
                        chosenIndex = position
                        calculated = false
                        render()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // No action required
                }
            }

        findViewById<View>(R.id.btnCalculateFees).setOnClickListener {
            val entered = count.text.toString().trim().toIntOrNull()

            if (entered == null || entered < 1 || entered > 10000) {
                count.error = "Enter a quantity from 1 to 10000"
                count.requestFocus()
                calculated = false
                render()
                return@setOnClickListener
            }

            chosenIndex = spinner.selectedItemPosition
            quantityDraft = entered.toString()

            experience = names[chosenIndex]
            unitPrice = prices[chosenIndex]
            quantity = entered

            subtotal = unitPrice * quantity

            discountRate = when {
                quantity >= 4 -> 15
                quantity == 3 -> 10
                quantity == 2 -> 5
                else -> 0
            }

            discount = subtotal * discountRate / 100.0
            discounted = subtotal - discount
            vat = discounted * 0.15
            total = discounted + vat

            calculated = true
            submitted = false
            reference = ""

            render()
        }

        next.setOnClickListener {
            if (
                !calculated ||
                spinner.selectedItemPosition != chosenIndex ||
                count.text.toString().trim().toIntOrNull() != quantity
            ) {
                calculated = false
                render()

                toast("Please calculate your current selection first")
            } else {
                show(R.layout.activity_booking)
            }
        }

        findViewById<View>(R.id.btnCalculateBack).setOnClickListener {
            back()
        }

        link(R.id.footerCalculateContact, R.layout.activity_contact)
    }

    // BOOKING FORM
    private fun booking() {
        if (!calculated) {
            show(R.layout.activity_calculate, false)
            return
        }

        setText(
            R.id.txtBookingExperience,
            "Experience: $experience"
        )

        setText(
            R.id.txtBookingQuantity,
            "Quantity: $quantity"
        )

        setText(
            R.id.txtBookingTotal,
            "Total: ${money(total)}"
        )

        field(R.id.edtBookingName).setText(name)
        field(R.id.edtBookingEmail).setText(email)
        field(R.id.edtBookingPhone).setText(phone)
        field(R.id.edtBookingDate).setText(date)
        field(R.id.edtBookingTime).setText(time)
        field(R.id.edtBookingRequests).setText(requests)

        // Preferred booking date
        val dateField = field(R.id.edtBookingDate)
        dateField.isFocusable = false

        dateField.setOnClickListener {
            val now = Calendar.getInstance()

            val picker = DatePickerDialog(
                this,
                { _, year, month, day ->
                    date = String.format(
                        Locale.US,
                        "%02d/%02d/%04d",
                        day,
                        month + 1,
                        year
                    )

                    dateField.setText(date)
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )

            picker.datePicker.minDate =
                System.currentTimeMillis() - 1000

            picker.show()
        }

        // Preferred booking time
        val timeField = field(R.id.edtBookingTime)
        timeField.isFocusable = false

        timeField.setOnClickListener {
            val now = Calendar.getInstance()

            TimePickerDialog(
                this,
                { _, hour, minute ->
                    time = String.format(
                        Locale.US,
                        "%02d:%02d",
                        hour,
                        minute
                    )

                    timeField.setText(time)
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
            ).show()
        }

        // Validate before displaying summary
        findViewById<View>(R.id.btnReviewBooking).setOnClickListener {
            saveVisibleDraft()

            if (name.isBlank()) {
                field(R.id.edtBookingName).error =
                    "Enter your full name"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                field(R.id.edtBookingEmail).error =
                    "Enter a valid email"
                return@setOnClickListener
            }

            val digits = phone.filter { it.isDigit() }

            if (digits.length !in 9..15) {
                field(R.id.edtBookingPhone).error =
                    "Enter a valid phone number"
                return@setOnClickListener
            }

            if (date.isBlank() || time.isBlank()) {
                toast("Please select a preferred date and time")
                return@setOnClickListener
            }

            show(R.layout.activity_summary)
        }

        findViewById<View>(R.id.btnBookingBack).setOnClickListener {
            back()
        }

        link(R.id.footerBookingContact, R.layout.activity_contact)
    }

    // BOOKING SUMMARY
    private fun summary() {
        setText(R.id.txtSummaryExperience, "Experience: $experience")
        setText(R.id.txtSummaryQuantity, "Quantity: $quantity")
        setText(R.id.txtSummaryUnitPrice, "Unit Price: ${money(unitPrice)}")

        setText(R.id.txtSummaryName, "Full Name: $name")
        setText(R.id.txtSummaryEmail, "Email: $email")
        setText(R.id.txtSummaryPhone, "Phone: $phone")
        setText(R.id.txtSummaryDate, "Preferred Date: $date")
        setText(R.id.txtSummaryTime, "Preferred Time: $time")

        setText(
            R.id.txtSummaryRequests,
            "Special Requests: ${requests.ifBlank { "None" }}"
        )

        setText(R.id.txtSummarySubtotal, "Subtotal: ${money(subtotal)}")

        setText(
            R.id.txtSummaryDiscount,
            "Discount ($discountRate%): -${money(discount)}"
        )

        setText(
            R.id.txtSummaryAfterDiscount,
            "After Discount: ${money(discounted)}"
        )

        setText(R.id.txtSummaryVat, "VAT (15%): ${money(vat)}")
        setText(R.id.txtSummaryGrandTotal, "TOTAL: ${money(total)}")

        // Confirm and save the booking request on this device
        findViewById<View>(R.id.btnConfirmBooking).setOnClickListener {
            if (
                !calculated ||
                name.isBlank() ||
                date.isBlank() ||
                time.isBlank()
            ) {
                toast("Please complete the calculator and booking details")
                return@setOnClickListener
            }

            if (!submitted) {
                reference = "NL-" +
                        UUID.randomUUID().toString()
                            .replace("-", "")
                            .take(8)
                            .uppercase(Locale.US)

                val record = JSONObject().apply {
                    put("reference", reference)
                    put("name", name)
                    put("email", email)
                    put("phone", phone)
                    put("experience", experience)
                    put("quantity", quantity)
                    put("unitPrice", unitPrice)
                    put("subtotal", subtotal)
                    put("discountPercent", discountRate)
                    put("discountAmount", discount)
                    put("vat", vat)
                    put("total", total)
                    put("preferredDate", date)
                    put("preferredTime", time)
                    put("requests", requests)
                    put("status", "Saved locally; not sent to arena")
                }

                val prefs = getSharedPreferences(
                    "next_level_records",
                    MODE_PRIVATE
                )

                val saved = prefs.edit()
                    .putString("booking_$reference", record.toString())
                    .putString("latest_booking", reference)
                    .commit()

                if (!saved) {
                    toast("Could not save the request. Please try again.")
                    return@setOnClickListener
                }

                submitted = true
            }

            show(R.layout.activity_confirmation)
        }

        findViewById<View>(R.id.btnSummaryBack).setOnClickListener {
            back()
        }

        link(R.id.footerSummaryContact, R.layout.activity_contact)
    }

    // BOOKING CONFIRMATION
    private fun confirmation() {
        setText(R.id.txtConfirmationReference, reference)
        setText(R.id.txtConfirmationName, "Customer: $name")

        setText(
            R.id.txtConfirmationExperience,
            "Experience: $experience"
        )

        setText(
            R.id.txtConfirmationQuantity,
            "Quantity: $quantity"
        )

        setText(
            R.id.txtConfirmationDate,
            "Preferred Date: $date"
        )

        setText(
            R.id.txtConfirmationTime,
            "Preferred Time: $time"
        )

        setText(
            R.id.txtConfirmationTotal,
            "TOTAL QUOTED: ${money(total)}"
        )

        findViewById<View>(R.id.btnConfirmationHome).setOnClickListener {
            history.clear()
            show(R.layout.activity_main, false)
        }

        findViewById<View>(R.id.btnConfirmationExperiences).setOnClickListener {
            history.clear()
            show(R.layout.activity_experiences, false)
        }

        link(
            R.id.footerConfirmationContact,
            R.layout.activity_contact
        )
    }

    // CONTACT US FORM
    private fun contact() {
        field(R.id.edtContactName).setText(contactName)
        field(R.id.edtContactEmail).setText(contactEmail)
        field(R.id.edtContactPhone).setText(contactPhone)
        field(R.id.edtContactSubject).setText(contactSubject)
        field(R.id.edtContactMessage).setText(contactMessage)

        findViewById<View>(R.id.btnSendEnquiry).setOnClickListener {
            saveVisibleDraft()

            if (contactName.isBlank()) {
                field(R.id.edtContactName).error = "Enter your name"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches()) {
                field(R.id.edtContactEmail).error = "Enter a valid email"
                return@setOnClickListener
            }

            val digits = contactPhone.filter { it.isDigit() }

            if (
                contactPhone.isNotBlank() &&
                digits.length !in 9..15
            ) {
                field(R.id.edtContactPhone).error =
                    "Enter a valid phone number"
                return@setOnClickListener
            }

            if (contactSubject.isBlank()) {
                field(R.id.edtContactSubject).error = "Enter a subject"
                return@setOnClickListener
            }

            if (contactMessage.isBlank()) {
                field(R.id.edtContactMessage).error = "Enter your message"
                return@setOnClickListener
            }

            val enquiryId = "ENQ-" +
                    UUID.randomUUID().toString()
                        .take(8)
                        .uppercase(Locale.US)

            val record = JSONObject().apply {
                put("reference", enquiryId)
                put("name", contactName)
                put("email", contactEmail)
                put("phone", contactPhone)
                put("subject", contactSubject)
                put("message", contactMessage)
                put("status", "Saved locally; not sent to arena")
            }

            val saved = getSharedPreferences(
                "next_level_records",
                MODE_PRIVATE
            ).edit()
                .putString("enquiry_$enquiryId", record.toString())
                .commit()

            if (saved) {
                toast(
                    "Enquiry saved on this device ($enquiryId). " +
                            "It has not been sent to the arena."
                )

                contactName = ""
                contactEmail = ""
                contactPhone = ""
                contactSubject = ""
                contactMessage = ""

                field(R.id.edtContactName).text.clear()
                field(R.id.edtContactEmail).text.clear()
                field(R.id.edtContactPhone).text.clear()
                field(R.id.edtContactSubject).text.clear()
                field(R.id.edtContactMessage).text.clear()
            } else {
                toast("Could not save the enquiry. Please try again.")
            }
        }

        findViewById<View>(R.id.btnContactBack).setOnClickListener {
            back()
        }

        findViewById<View>(R.id.footerContactLink).setOnClickListener {
            findViewById<View>(R.id.edtContactName).requestFocus()
        }
    }

    // Preserve text when moving between screens
    private fun saveVisibleDraft() {
        when (currentScreen) {
            R.layout.activity_calculate -> {
                findViewById<Spinner>(R.id.spinnerExperience)?.let {
                    chosenIndex = it.selectedItemPosition.coerceAtLeast(0)
                }

                findViewById<EditText>(R.id.edtQuantity)?.let {
                    quantityDraft = it.text.toString()
                }
            }

            R.layout.activity_booking -> {
                name = value(R.id.edtBookingName)
                email = value(R.id.edtBookingEmail)
                phone = value(R.id.edtBookingPhone)
                date = value(R.id.edtBookingDate)
                time = value(R.id.edtBookingTime)
                requests = value(R.id.edtBookingRequests)
            }

            R.layout.activity_contact -> {
                contactName = value(R.id.edtContactName)
                contactEmail = value(R.id.edtContactEmail)
                contactPhone = value(R.id.edtContactPhone)
                contactSubject = value(R.id.edtContactSubject)
                contactMessage = value(R.id.edtContactMessage)
            }
        }
    }

    // HAMBURGER NAVIGATION MENU
    private fun setupMenu() {
        val id = when (currentScreen) {
            R.layout.activity_main -> R.id.btnMenu
            R.layout.activity_about -> R.id.btnAboutMenu
            R.layout.activity_experiences -> R.id.btnExperiencesMenu
            R.layout.activity_vr -> R.id.btnVrMenu
            R.layout.activity_racing -> R.id.btnRacingMenu
            R.layout.activity_escape -> R.id.btnEscapeMenu
            R.layout.activity_ultimate -> R.id.btnUltimateMenu
            R.layout.activity_vip -> R.id.btnVipMenu
            R.layout.activity_esports -> R.id.btnEsportsMenu
            R.layout.activity_birthday -> R.id.btnBirthdayMenu
            R.layout.activity_calculate -> R.id.btnCalculateMenu
            R.layout.activity_contact -> R.id.btnContactMenu
            R.layout.activity_booking -> R.id.btnBookingMenu
            R.layout.activity_summary -> R.id.btnSummaryMenu
            R.layout.activity_confirmation -> R.id.btnConfirmationMenu
            else -> return
        }

        findViewById<View>(id).setOnClickListener { anchor ->
            val popup = PopupMenu(this, anchor)

            popup.menu.add(Menu.NONE, 1, 1, "Home")
            popup.menu.add(Menu.NONE, 2, 2, "About Us")
            popup.menu.add(Menu.NONE, 3, 3, "Our Experiences")
            popup.menu.add(Menu.NONE, 4, 4, "Calculate Fees / Book Now")
            popup.menu.add(Menu.NONE, 5, 5, "Contact Us")

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    1 -> show(R.layout.activity_main)
                    2 -> show(R.layout.activity_about)
                    3 -> show(R.layout.activity_experiences)
                    4 -> show(R.layout.activity_calculate)
                    5 -> show(R.layout.activity_contact)
                }

                true
            }

            popup.show()
        }
    }

    // ANDROID BACK BUTTON
    private fun back() {
        if (history.isNotEmpty()) {
            show(
                history.removeAt(history.lastIndex),
                false
            )
        } else if (currentScreen != R.layout.activity_main) {
            show(R.layout.activity_main, false)
        } else {
            finish()
        }
    }
}
