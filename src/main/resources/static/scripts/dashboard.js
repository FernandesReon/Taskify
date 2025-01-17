// Card dropdown
document.addEventListener("DOMContentLoaded", () => {
    const dropdownButtons = document.querySelectorAll(".drop-down-btn");

    dropdownButtons.forEach((button) => {
        button.addEventListener("click", () => {
            const dropdown = button.nextElementSibling;

            // Toggle the clicked dropdown
            if (dropdown.classList.contains("show")) {
                dropdown.classList.remove("show");
            } else {
                // Close any other open dropdowns
                closeAllDropdowns();
                dropdown.classList.add("show");
            }
        });
    });

    // Close dropdowns when clicking outside
    document.addEventListener("click", (event) => {
        if (!event.target.classList.contains("drop-down-btn")) {
            closeAllDropdowns();
        }
    });

    // Function to close all dropdowns
    function closeAllDropdowns() {
        const allDropdowns = document.querySelectorAll(".card-dropdown-content");
        allDropdowns.forEach((dropdown) => {
            dropdown.classList.remove("show");
        });
    }
});


// Action button dropdown
document.addEventListener("DOMContentLoaded", function () {
    const actionBtn = document.getElementById("action-btn");
    const actionContent = document.getElementById("action-content");

    // Toggle dropdown on button click
    actionBtn.addEventListener("click", function (event) {
        event.stopPropagation(); // Prevent event from bubbling to document
        actionContent.style.display = actionContent.style.display === "block" ? "none" : "block";
    });

    // Close the dropdown if clicking outside
    document.addEventListener("click", function (event) {
        if (!actionContent.contains(event.target) && !actionBtn.contains(event.target)) {
            actionContent.style.display = "none";
        }
    });
});