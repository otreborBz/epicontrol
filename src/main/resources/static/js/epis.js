document.addEventListener("DOMContentLoaded", function () {
  // --- Lógica para o modal de NOVO/EDITAR EPI ---
  const epiModal = document.getElementById("epi-modal");
  const modalForm = epiModal.querySelector("form");
  const modalTitle = epiModal.querySelector("h2");

  // Cria um input hidden para o ID, que será usado na edição
  const hiddenIdInput = document.createElement("input");
  hiddenIdInput.type = "hidden";
  hiddenIdInput.name = "id";
  modalForm.appendChild(hiddenIdInput);

  const openNovoEpiBtn = document.getElementById("open-modal-btn");
  const closeEpiBtn = document.getElementById("close-modal-btn");

  if (epiModal && openNovoEpiBtn && closeEpiBtn) {
    // Abrir modal para NOVO EPI
    openNovoEpiBtn.onclick = () => {
      modalForm.reset();
      modalTitle.textContent = "Novo EPI";
      modalForm.action = "/epis";
      hiddenIdInput.value = "";
      epiModal.style.display = "flex";
    };
    // Fechar modal
    closeEpiBtn.onclick = () => {
      epiModal.style.display = "none";
    };
  }

  // --- Lógica para o modal de EDIÇÃO ---
  const openEditModalBtns = document.querySelectorAll(".open-edit-modal-btn");

  openEditModalBtns.forEach((btn) => {
    btn.addEventListener("click", function () {
      const epiId = this.getAttribute("data-id");

      // Busca os dados do EPI na API
      fetch(`/api/epis/${epiId}`)
        .then((response) => {
          if (!response.ok) {
            throw new Error("Falha ao buscar dados do EPI.");
          }
          return response.json();
        })
        .then((data) => {
          // Configura o modal para "Editar EPI"
          modalTitle.textContent = "Editar EPI";
          modalForm.action = "/epis/edit";

          // Preenche o formulário com os dados retornados
          modalForm.querySelector("#nome").value = data.nome || "";
          modalForm.querySelector("#ca").value = data.ca || "";
          // A data vem como um array [ano, mes, dia], então formatamos para YYYY-MM-DD
          if (Array.isArray(data.validade) && data.validade.length >= 3) {
            const [year, month, day] = data.validade;
            modalForm.querySelector("#validade").value = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
          }
          modalForm.querySelector("#quantidade").value = data.quantidade || "";
          hiddenIdInput.value = data.id || "";

          // Abre o modal
          epiModal.style.display = "flex";
        })
        .catch((error) => console.error("Erro:", error));
    });
  });

  // --- Lógica para o modal de EXCLUSÃO ---
  const deleteConfirmModal = document.getElementById("delete-confirm-modal");
  const openDeleteModalBtns = document.querySelectorAll(".open-delete-modal-btn");
  const closeDeleteModalBtn = document.getElementById("close-delete-modal-btn");
  const cancelDeleteBtn = document.getElementById("cancel-delete-btn");
  const epiIdInput = document.getElementById("epi-id-to-delete");

  if (deleteConfirmModal) {
    // Abrir o modal de exclusão
    openDeleteModalBtns.forEach((btn) => {
      btn.addEventListener("click", function () {
        const epiId = this.getAttribute("data-id");
        if (epiId) {
          epiIdInput.value = epiId;
          deleteConfirmModal.style.display = "flex";
        }
      });
    });

    // Função para fechar o modal de exclusão
    const closeDeleteModal = () => {
      deleteConfirmModal.style.display = "none";
      epiIdInput.value = "";
    };

    if (closeDeleteModalBtn) closeDeleteModalBtn.onclick = closeDeleteModal;
    if (cancelDeleteBtn) cancelDeleteBtn.onclick = closeDeleteModal;
  }

  // Fechar modais ao clicar fora do conteúdo
  window.onclick = function (event) {
    if (event.target == epiModal) {
      epiModal.style.display = "none";
    }
    if (event.target == deleteConfirmModal) {
      deleteConfirmModal.style.display = "none";
    }
  };
});