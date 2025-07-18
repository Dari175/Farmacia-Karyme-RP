<template id="welcome-page">
    <app-frame>
        <v-container fluid class="fill-height">
            <v-row align="center" justify="center" class="fill-height">
                <v-col cols="12">
                    <!-- Hero Section -->
                    <v-card
                        class="mx-auto elevation-8 rounded-xl"
                        max-width="800"
                        color="gradient-primary"
                    >
                        <v-card-text class="text-center pa-8">
                            <v-avatar size="120" class="mb-6 elevation-4">
                                <v-img
                                    src="https://avatars.githubusercontent.com/u/158988046?v=4"
                                    alt="Arturo Lopez"
                                ></v-img>
                            </v-avatar>

                            <h1 class="text-h3 font-weight-bold text-white mb-2">
                                ¡Bienvenido, Arturo! 👋
                            </h1>

                            <p class="text-h6 text-white mb-4 opacity-90">
                                Tu centro de control de datos
                            </p>

                            <v-chip
                                color="white"
                                text-color="primary"
                                size="large"
                                prepend-icon="mdi-star"
                                class="ma-2"
                            >
                                Desarrollado con Vuetify
                            </v-chip>
                        </v-card-text>
                    </v-card>
                </v-col>
            </v-row>

            <!-- Stats Cards -->
            <v-row class="mt-6">
                <v-col v-for="stat in stats" :key="stat.title" cols="12" sm="6" md="3">
                    <v-card
                        class="elevation-2 rounded-lg hover-lift"
                        :color="stat.color"
                        dark
                    >
                        <v-card-text class="text-center pa-6">
                            <v-icon size="40" class="mb-3">{{ stat.icon }}</v-icon>
                            <div class="text-h4 font-weight-bold mb-1">{{ stat.value }}</div>
                            <div class="text-subtitle-1">{{ stat.title }}</div>
                        </v-card-text>
                    </v-card>
                </v-col>
            </v-row>

            <!-- Quick Actions -->
            <v-row class="mt-6">
                <v-col cols="12">
                    <v-card class="elevation-2 rounded-lg">
                        <v-card-title class="text-h5 pa-6">
                            <v-icon left class="mr-3">mdi-lightning-bolt</v-icon>
                            Acciones Rápidas
                        </v-card-title>

                        <v-card-text class="pa-6 pt-0">
                            <v-row>
                                <v-col v-for="action in quickActions" :key="action.title" cols="12" sm="6" md="4">
                                    <v-card
                                        class="elevation-1 rounded-lg hover-lift cursor-pointer"
                                        :href="action.href"
                                        :color="action.color"
                                        variant="tonal"
                                    >
                                        <v-card-text class="text-center pa-4">
                                            <v-icon size="32" class="mb-2">{{ action.icon }}</v-icon>
                                            <div class="text-h6 font-weight-medium mb-1">{{ action.title }}</div>
                                            <div class="text-caption">{{ action.description }}</div>
                                        </v-card-text>
                                    </v-card>
                                </v-col>
                            </v-row>
                        </v-card-text>
                    </v-card>
                </v-col>
            </v-row>

            <!-- Recent Activity -->
            <v-row class="mt-6">
                <v-col cols="12" md="8">
                    <v-card class="elevation-2 rounded-lg">
                        <v-card-title class="text-h5 pa-6">
                            <v-icon left class="mr-3">mdi-clock-outline</v-icon>
                            Actividad Reciente
                        </v-card-title>

                        <v-card-text class="pa-0">
                            <v-list>
                                <v-list-item
                                    v-for="activity in recentActivity"
                                    :key="activity.id"
                                    :prepend-icon="activity.icon"
                                    :title="activity.title"
                                    :subtitle="activity.subtitle"
                                    class="border-b"
                                >
                                    <template v-slot:append>
                                        <v-chip
                                            :color="activity.status === 'success' ? 'success' : 'warning'"
                                            size="small"
                                            variant="flat"
                                        >
                                            {{ activity.status === 'success' ? 'Completado' : 'Pendiente' }}
                                        </v-chip>
                                    </template>
                                </v-list-item>
                            </v-list>
                        </v-card-text>
                    </v-card>
                </v-col>

                <v-col cols="12" md="4">
                    <v-card class="elevation-2 rounded-lg">
                        <v-card-title class="text-h5 pa-6">
                            <v-icon left class="mr-3">mdi-information</v-icon>
                            Información del Sistema
                        </v-card-title>

                        <v-card-text class="pa-6 pt-0">
                            <v-list density="compact">
                                <v-list-item
                                    prepend-icon="mdi-account"
                                    title="Usuario"
                                    subtitle="Arturo Lopez"
                                ></v-list-item>

                                <v-list-item
                                    prepend-icon="mdi-calendar"
                                    title="Último acceso"
                                    :subtitle="lastAccess"
                                ></v-list-item>

                                <v-list-item
                                    prepend-icon="mdi-vuetify"
                                    title="Versión"
                                    subtitle="Vuetify 3.x"
                                ></v-list-item>

                                <v-list-item
                                    prepend-icon="mdi-server"
                                    title="Estado"
                                    subtitle="Conectado"
                                >
                                    <template v-slot:append>
                                        <v-icon color="success">mdi-check-circle</v-icon>
                                    </template>
                                </v-list-item>
                            </v-list>
                        </v-card-text>
                    </v-card>
                </v-col>
            </v-row>

            <!-- Footer -->
            <v-row class="mt-6">
                <v-col cols="12">
                    <v-card class="elevation-1 rounded-lg" color="grey-lighten-5">
                        <v-card-text class="text-center pa-4">
                            <div class="text-body-2 text-grey-darken-1">

                                Personalizado para Arturo Lopez
                            </div>
                        </v-card-text>
                    </v-card>
                </v-col>
            </v-row>
        </v-container>
    </app-frame>
</template>

<script>
app.component("welcome-page", {
    template: "#welcome-page",
    data: () => ({
        stats: [
            {
                title: "Tablas Activas",
                value: "5",
                icon: "mdi-table",
                color: "primary"
            },
            {
                title: "Registros",
                value: "237",
                icon: "mdi-database",
                color: "success"
            },
            {
                title: "Reportes",
                value: "12",
                icon: "mdi-chart-line",
                color: "info"
            },
            {
                title: "Usuarios",
                value: "8",
                icon: "mdi-account-group",
                color: "warning"
            }
        ],

        quickActions: [
            {
                title: "Ver Tablas",
                description: "Acceder a todas las tablas",
                icon: "mdi-table-large",
                href: "/tables",
                color: "primary"
            },
            {
                title: "Dashboard",
                description: "Ver métricas y análisis",
                icon: "mdi-view-dashboard",
                href: "/dashboard",
                color: "success"
            },
            {
                title: "Crear Reporte",
                description: "Generar nuevo reporte",
                icon: "mdi-file-plus",
                href: "/reports/new",
                color: "info"
            },
            {
                title: "Configuración",
                description: "Ajustar preferencias",
                icon: "mdi-cog",
                href: "/settings",
                color: "warning"
            },
            {
                title: "Biblioteca",
                description: "150 libros disponibles",
                icon: "mdi-book-open-variant",
                href: "/books",
                color: "deep-purple"
            },
            {
                title: "Exportar Datos",
                description: "Descargar información",
                icon: "mdi-download",
                href: "#",
                color: "teal"
            }
        ],

        recentActivity: [
            {
                id: 1,
                title: "Tabla de Libros actualizada",
                subtitle: "Hace 2 horas",
                icon: "mdi-book",
                status: "success"
            },
            {
                id: 2,
                title: "Nuevo reporte generado",
                subtitle: "Hace 4 horas",
                icon: "mdi-file-chart",
                status: "success"
            },
            {
                id: 3,
                title: "Sincronización de datos",
                subtitle: "Hace 6 horas",
                icon: "mdi-sync",
                status: "pending"
            },
            {
                id: 4,
                title: "Backup completado",
                subtitle: "Hace 1 día",
                icon: "mdi-backup-restore",
                status: "success"
            }
        ],

        lastAccess: new Date().toLocaleDateString('es-ES', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        })
    })
})
</script>

<style scoped>
.gradient-primary {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.hover-lift {
    transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
}

.hover-lift:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(0,0,0,0.15);
}

.cursor-pointer {
    cursor: pointer;
}

.border-b {
    border-bottom: 1px solid rgba(0,0,0,0.1);
}

.border-b:last-child {
    border-bottom: none;
}
</style>