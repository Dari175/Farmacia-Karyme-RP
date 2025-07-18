<template id="app-frame">
  <v-card class="fill-height">
    <v-layout>
      <!-- Navigation Drawer mejorado -->
      <v-navigation-drawer
        expand-on-hover
        rail
        class="elevation-3"
        color="primary"
        dark
        width="280"
      >
        <!-- Perfil del usuario -->
        <v-list class="pa-0">
          <v-list-item
            prepend-avatar="https://avatars.githubusercontent.com/u/158988046?v=4"
            subtitle="arturo.lopez@email.com"
            title="Arturo Lopez"
            class="mb-2 pa-3"
          >
            <template v-slot:append>
              <v-btn
                icon="mdi-account-settings"
                size="small"
                variant="text"
                @click="openSettings"
                class="ml-2"
              ></v-btn>
            </template>
          </v-list-item>
        </v-list>

        <v-divider class="mx-3 mb-3" color="white" opacity="0.3"></v-divider>

        <!-- Enlaces principales -->
        <v-list density="compact" nav class="pa-2">
          <v-list-subheader class="text-white opacity-70 font-weight-medium">
            NAVEGACIÓN PRINCIPAL
          </v-list-subheader>

          <v-list-item
            v-for="item in mainLinks"
            :key="item.title"
            :prepend-icon="item.icon"
            :title="item.title"
            :href="item.path"
            class="mb-1 rounded-lg nav-item"
            :class="{ 'v-list-item--active': isActive(item.path) }"
          >
            <template v-slot:append>
              <v-badge
                v-if="item.badge"
                :content="item.badge"
                color="accent"
                inline
              ></v-badge>
            </template>
          </v-list-item>
        </v-list>

        <v-divider class="mx-3 my-3" color="white" opacity="0.3"></v-divider>

        <!-- Tablas de ejemplo integradas -->
        <v-list density="compact" nav class="pa-2">
          <v-list-subheader class="text-white opacity-70 font-weight-medium">
            TABLAS DE DATOS
          </v-list-subheader>

          <v-list-item
            v-for="table in table_examples"
            :key="table.id"
            :prepend-icon="table.icon"
            :title="table.name"
            :href="table.path"
            class="mb-1 rounded-lg nav-item"
            :class="{ 'v-list-item--active': isActive(table.path) }"
          >
            <template v-slot:append>
              <v-chip
                v-if="table.count"
                :text="table.count"
                size="x-small"
                color="rgba(255,255,255,0.2)"
                variant="flat"
                class="text-white"
              ></v-chip>
            </template>
          </v-list-item>
        </v-list>

        <v-divider class="mx-3 my-3" color="white" opacity="0.3"></v-divider>

        <!-- Acciones rápidas -->
        <v-list density="compact" nav class="pa-2">
          <v-list-subheader class="text-white opacity-70 font-weight-medium">
            ACCIONES RÁPIDAS
          </v-list-subheader>

          <v-list-item
            title="Actualizar datos"
            prepend-icon="mdi-refresh"
            @click="refreshData"
            class="mb-1 rounded-lg nav-item action-item"
          >
            <template v-slot:append>
              <v-icon size="small" color="success">mdi-chevron-right</v-icon>
            </template>
          </v-list-item>

          <v-list-item
            title="Exportar datos"
            prepend-icon="mdi-download"
            @click="exportData"
            class="mb-1 rounded-lg nav-item action-item"
          >
            <template v-slot:append>
              <v-icon size="small" color="info">mdi-chevron-right</v-icon>
            </template>
          </v-list-item>

          <v-list-item
            title="Importar datos"
            prepend-icon="mdi-upload"
            @click="importData"
            class="mb-1 rounded-lg nav-item action-item"
          >
            <template v-slot:append>
              <v-icon size="small" color="warning">mdi-chevron-right</v-icon>
            </template>
          </v-list-item>
        </v-list>

        <!-- Botón de logout -->
        <template v-slot:append>
          <div class="pa-3">
            <v-btn
              prepend-icon="mdi-logout"
              variant="outlined"
              color="white"
              @click="logout"
              block
              class="text-white border-white"
            >
              Cerrar sesión
            </v-btn>
          </div>
        </template>
      </v-navigation-drawer>

      <!-- Main Content Area -->
      <v-main class="bg-grey-lighten-4">
        <v-container fluid class="pa-4">
          <!-- Header con breadcrumbs y acciones -->
          <v-row class="mb-4">
            <v-col>
              <v-card rounded="lg" class="elevation-2 pa-4" color="white">
                <v-row align="center">
                  <v-col>
                    <v-breadcrumbs
                      :items="breadcrumbs"
                      class="pa-0"
                      divider="/"
                    >
                      <template v-slot:prepend>
                        <v-icon size="small" color="primary">mdi-home</v-icon>
                      </template>
                      <template v-slot:item="{ item }">
                        <v-breadcrumbs-item
                          :href="item.href"
                          :disabled="item.disabled"
                          class="text-body-2"
                        >
                          {{ item.title }}
                        </v-breadcrumbs-item>
                      </template>
                    </v-breadcrumbs>
                  </v-col>
                  <v-col cols="auto">
                    <v-btn-group variant="outlined" divided>
                      <v-btn
                        icon="mdi-refresh"
                        size="small"
                        @click="refreshData"
                        title="Actualizar"
                      ></v-btn>
                      <v-btn
                        icon="mdi-fullscreen"
                        size="small"
                        @click="toggleFullscreen"
                        title="Pantalla completa"
                      ></v-btn>
                      <v-btn
                        icon="mdi-help-circle"
                        size="small"
                        @click="showHelp"
                        title="Ayuda"
                      ></v-btn>
                    </v-btn-group>
                  </v-col>
                </v-row>
              </v-card>
            </v-col>
          </v-row>

          <!-- Content Area -->
          <v-row>
            <v-col>
              <v-card
                rounded="lg"
                class="elevation-2 pa-6 fill-height"
                color="white"
                min-height="600"
              >
                <!-- Slot para contenido -->
                <div class="content-area">
                  <slot>
                    <!-- Contenido por defecto -->
                    <div class="text-center pa-8">
                      <v-icon size="64" color="grey-lighten-2" class="mb-4">mdi-view-dashboard</v-icon>
                      <h2 class="text-h5 mb-3 text-grey-darken-1">Bienvenido al Dashboard</h2>
                      <p class="text-body-1 text-grey-darken-1 mb-4">
                        Selecciona una opción del menú lateral para comenzar
                      </p>
                      <v-btn
                        color="primary"
                        variant="elevated"
                        size="large"
                        prepend-icon="mdi-rocket-launch"
                        @click="showNotification('¡Empezando!', 'success')"
                      >
                        Comenzar
                      </v-btn>
                    </div>
                  </slot>
                </div>
              </v-card>
            </v-col>
          </v-row>
        </v-container>
      </v-main>
    </v-layout>

    <!-- Snackbar para notificaciones -->
    <v-snackbar
      v-model="snackbar.show"
      :color="snackbar.color"
      :timeout="snackbar.timeout"
      location="top right"
      variant="elevated"
    >
      <v-icon class="mr-2">{{ snackbar.icon }}</v-icon>
      {{ snackbar.message }}
      <template v-slot:actions>
        <v-btn
          variant="text"
          @click="snackbar.show = false"
          icon="mdi-close"
        ></v-btn>
      </template>
    </v-snackbar>
  </v-card>
</template>

<script>
app.component("app-frame", {
  template: "#app-frame",
  data: () => ({
    // Enlaces principales
    mainLinks: [
      {
        title: "Dashboard",
        path: "/",
        icon: "mdi-view-dashboard"
      },
      {
        title: "Reportes",
        path: "/reports",
        icon: "mdi-chart-line",
        badge: "3"
      },
      {
        title: "Configuración",
        path: "/settings",
        icon: "mdi-cog"
      }
    ],

    // Tablas de ejemplo mejoradas
    table_examples: [
      {
        id: 1,
        path: "/books",
        name: "Biblioteca",
        icon: "mdi-book-open-variant",
        count: "150"
      },
      {
        id: 2,
        path: "/desserts",
        name: "Postres",
        icon: "mdi-cake-variant",
        count: "25"
      },
      {
        id: 3,
        path: "/mice",
        name: "Ratones",
        icon: "mdi-mouse-variant",
        count: "8"
      },
      {
        id: 4,
        path: "/games",
        name: "Videojuegos",
        icon: "mdi-gamepad-variant",
        count: "42"
      },
      {
        id: 5,
        path: "/idesserts",
        name: "Postres Premium",
        icon: "mdi-cupcake",
        count: "12"
      },
      {
        id: 6,
        path: "/users",
        name: "Usuarios",
        icon: "mdi-account-group",
        count: "89"
      }
    ],

    // Breadcrumbs dinámicos
    breadcrumbs: [
      { title: "Inicio", href: "/" },
      { title: "Actual", disabled: true }
    ],

    // Sistema de notificaciones mejorado
    snackbar: {
      show: false,
      message: '',
      color: 'success',
      timeout: 4000,
      icon: 'mdi-check-circle'
    }
  }),

  methods: {
    // Verificar si un enlace está activo
    isActive(path) {
      return window.location.pathname === path;
    },

    // Abrir configuración de usuario
    openSettings() {
      this.showNotification('Abriendo configuración de usuario...', 'info', 'mdi-cog');
    },

    // Cerrar sesión
    logout() {
      this.showNotification('Cerrando sesión...', 'warning', 'mdi-logout');
      // Aquí iría la lógica de logout
    },

    // Actualizar datos
    refreshData() {
      this.showNotification('Actualizando datos...', 'info', 'mdi-refresh');
      // Aquí iría la lógica de actualización
    },

    // Exportar datos
    exportData() {
      this.showNotification('Exportando datos...', 'success', 'mdi-download');
      // Aquí iría la lógica de exportación
    },

    // Importar datos
    importData() {
      this.showNotification('Importando datos...', 'info', 'mdi-upload');
      // Aquí iría la lógica de importación
    },

    // Pantalla completa
    toggleFullscreen() {
      this.showNotification('Cambiando modo de pantalla...', 'info', 'mdi-fullscreen');
    },

    // Mostrar ayuda
    showHelp() {
      this.showNotification('Mostrando ayuda...', 'info', 'mdi-help-circle');
    },

    // Mostrar notificación mejorada
    showNotification(message, color = 'success', icon = 'mdi-check-circle') {
      this.snackbar = {
        show: true,
        message,
        color,
        icon,
        timeout: 4000
      };
    },

    // Actualizar breadcrumbs
    updateBreadcrumbs() {
      const path = window.location.pathname;
      const segments = path.split('/').filter(Boolean);

      this.breadcrumbs = [
        { title: "Inicio", href: "/" }
      ];

      let currentPath = '';
      segments.forEach(segment => {
        currentPath += `/${segment}`;
        const linkItem = this.mainLinks.find(link => link.path === currentPath);
        const tableItem = this.table_examples.find(table => table.path === currentPath);

        if (linkItem) {
          this.breadcrumbs.push({
            title: linkItem.title,
            href: currentPath
          });
        } else if (tableItem) {
          this.breadcrumbs.push({
            title: tableItem.name,
            href: currentPath
          });
        } else {
          this.breadcrumbs.push({
            title: segment.charAt(0).toUpperCase() + segment.slice(1),
            href: currentPath
          });
        }
      });

      // Marcar el último como disabled
      if (this.breadcrumbs.length > 1) {
        this.breadcrumbs[this.breadcrumbs.length - 1].disabled = true;
      }
    }
  },

  // Actualizar breadcrumbs basado en la ruta actual
  mounted() {
    this.updateBreadcrumbs();
  }
})
</script>

<style scoped>
.content-area {
  min-height: 500px;
}

/* Estilos mejorados para el navigation drawer */
.v-navigation-drawer .nav-item {
  transition: all 0.3s ease;
  margin-bottom: 2px;
}

.v-navigation-drawer .nav-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
  transform: translateX(4px);
}

.v-navigation-drawer .v-list-item--active {
  background-color: rgba(255, 255, 255, 0.15);
  border-left: 3px solid white;
}

.v-navigation-drawer .action-item:hover {
  background-color: rgba(255, 255, 255, 0.08);
}

/* Mejorar el estilo de los subheaders */
.v-navigation-drawer .v-list-subheader {
  font-size: 0.75rem;
  letter-spacing: 0.5px;
  text-transform: uppercase;
  margin-bottom: 8px;
}

/* Animaciones suaves */
.v-card {
  transition: all 0.3s ease;
}

.v-btn {
  transition: all 0.2s ease;
}

.v-btn:hover {
  transform: translateY(-1px);
}

/* Mejorar el estilo de los breadcrumbs */
.v-breadcrumbs {
  font-weight: 500;
}

.v-breadcrumbs-item {
  color: #666;
}

.v-breadcrumbs-item:hover {
  color: #1976d2;
}

/* Efectos de sombra */
.elevation-2 {
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.08) !important;
}

.elevation-3 {
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15), 0 2px 4px rgba(0, 0, 0, 0.12) !important;
}
</style>