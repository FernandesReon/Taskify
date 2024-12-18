const desktopView = document.querySelector(".navbar");
const mobileView = document.querySelector(".mobile-view");
const menuIcon = document.querySelector(".ion-icon[name = 'menu']");
const closeIcon = document.querySelector(".ion-icon[name = 'close-circle']");

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