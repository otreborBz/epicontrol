document.addEventListener("DOMContentLoaded", function () {
  const modal = document.getElementById("epi-modal");
  const openModalBtn = document.getElementById("open-modal-btn");
  const closeModalBtn = document.getElementById("close-modal-btn");

  if (modal && openModalBtn && closeModalBtn) {
    // Função para abrir o modal
    openModalBtn.onclick = function () {
      modal.style.display = "flex";
    };

    // Função para fechar o modal
    closeModalBtn.onclick = function () {
      modal.style.display = "none";
    };

    // Fechar o modal se o usuário clicar fora do conteúdo
    window.onclick = function (event) {
      if (event.target == modal) {
        modal.style.display = "none";
      }
    };
  }
});