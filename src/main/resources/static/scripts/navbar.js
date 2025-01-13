const mobileMenu = document.getElementById('mobile-menu');
const navLinks = document.querySelector('.nav-links');
const menuIcon = document.getElementById('menu-icon');
const closeIcon = document.getElementById('close-icon');

const toggleNavbar = () => {
    navLinks.classList.toggle("active");

    if (navLinks.classList.contains("active")){
        menuIcon.style.display = "none";
        closeIcon.style.display = "block";
    }
    else {
        menuIcon.style.display = "block";
        closeIcon.style.display = "none";
    }

}

mobileMenu.addEventListener("click", () => toggleNavbar());