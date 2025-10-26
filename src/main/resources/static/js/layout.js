document.addEventListener("DOMContentLoaded", function () {
  const toggleButton = document.getElementById("sidebar-toggle");
  const root = document.documentElement;

  // Adiciona o evento apenas se o botão existir na página
  if (toggleButton) {
    toggleButton.addEventListener("click", () => {
      root.classList.toggle("sidebar-collapsed");
      localStorage.setItem(
        "sidebarCollapsed",
        root.classList.contains("sidebar-collapsed")
      );
    });
  }
});