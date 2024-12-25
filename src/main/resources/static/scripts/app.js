// Mobile navigation
const desktopView = document.querySelector(".navbar");
const mobileView = document.querySelector(".mobile-view");
const menuIcon = document.querySelector(".ion-icon[name='menu']");
const closeIcon = document.querySelector(".ion-icon[name='close-circle']");

const toggleNavbar = () =>{
    desktopView.classList.toggle("active");

    if (desktopView.classList.contains("active")){
        menuIcon.style.display = "none";
        closeIcon.style.display = "block";
    }
    else{
        menuIcon.style.display = "block";
        closeIcon.style.display = "none";
    }
}
mobileView.addEventListener("click", () => toggleNavbar());


// To display alert messages in register form
document.addEventListener('DOMContentLoaded', function () {
    const successMessage = document.getElementById('success-msg');
    const errorMessage = document.getElementById('failure-msg');

    // Check if either message exists
    if (successMessage || errorMessage) {
        // Set a timeout to hide the message after 5 seconds
        setTimeout(() => {
            if (successMessage) {
                successMessage.style.display = 'none';
            }
            if (errorMessage) {
                errorMessage.style.display = 'none';
            }
        }, 5000);
    }
});