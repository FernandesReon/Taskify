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
