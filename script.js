// 1. Swap a placeholder for the real image when its file exists in /assets.
document.querySelectorAll("[data-img]").forEach(function (box) {
  const img = new Image();
  img.onload = function () {
    img.alt = box.dataset.alt || "";
    box.replaceChildren(img);
  };
  img.src = box.dataset.img;
});

// 2. Highlight the nav link of the section currently in view (home page only).
const navLinks = document.querySelectorAll('nav a[href^="#"]');
if ("IntersectionObserver" in window && navLinks.length) {
  const observer = new IntersectionObserver(function (entries) {
    entries.forEach(function (entry) {
      if (!entry.isIntersecting) return;
      navLinks.forEach(function (link) {
        link.classList.toggle("on", link.hash === "#" + entry.target.id);
      });
    });
  }, { rootMargin: "-40% 0px -55% 0px" });
  navLinks.forEach(function (link) {
    const section = document.querySelector(link.hash);
    if (section) observer.observe(section);
  });
}
