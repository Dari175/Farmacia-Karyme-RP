<template id="antidoping-page">
  <div class="min-h-screen bg-white">
    <!-- Formulario -->
    <div v-if="currentView === 'form'" class="min-h-screen bg-gray-50">
      <!-- Header sticky optimizado para móvil -->
      <div class="sticky top-0 bg-white shadow-sm z-10 p-3 sm:p-4">
        <div class="flex items-center">
          <button
            type="button"
            @click="$emit('back')"
            class="mr-3 p-2 text-gray-600 hover:bg-gray-100 rounded-full touch-manipulation"
          >
            <v-icon>mdi-arrow-left</v-icon>
          </button>
          <h2 class="text-lg sm:text-2xl font-bold text-gray-800">Antidoping</h2>
        </div>
      </div>

      <!-- Contenido principal con padding bottom para botón flotante -->
      <div class="p-3 sm:p-4 pb-20">
        <div class="max-w-md mx-auto sm:max-w-2xl">
          <div class="bg-white rounded-lg shadow-lg p-4 sm:p-6">
            <!-- Validación de errores -->
            <div v-if="errors.length > 0" class="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg">
              <div class="flex items-center mb-2">
                <v-icon color="red" class="mr-2">mdi-alert-circle</v-icon>
                <span class="text-red-700 font-medium">Por favor completa los siguientes campos:</span>
              </div>
              <ul class="text-sm text-red-600 list-disc list-inside">
                <li v-for="(error, index) in errors" :key="index">{{ error }}</li>
              </ul>
            </div>

            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Nombre del Paciente *
                </label>
                <input
                  type="text"
                  v-model="formData.nombre"
                  @input="clearErrors"
                  :class="[
                    'w-full p-3 text-base font-medium border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent',
                    errors.includes('Nombre del paciente') ? 'border-red-300 bg-red-50' : 'border-gray-300'
                  ]"
                  placeholder="Nombre completo"
                  autocomplete="off"
                />
              </div>

              <!-- Grid responsivo para fecha y edad -->
              <div class="space-y-4 sm:grid sm:grid-cols-2 sm:gap-4 sm:space-y-0">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    Fecha *
                  </label>
                  <input
                    type="date"
                    v-model="formData.fecha"
                    @input="clearErrors"
                    :class="[
                      'w-full p-3 text-base font-medium border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent',
                      errors.includes('Fecha') ? 'border-red-300 bg-red-50' : 'border-gray-300'
                    ]"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    Edad *
                  </label>
                  <input
                    type="number"
                    v-model="formData.edad"
                    @input="clearErrors"
                    :class="[
                      'w-full p-3 text-base font-medium border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent',
                      errors.includes('Edad') ? 'border-red-300 bg-red-50' : 'border-gray-300'
                    ]"
                    placeholder="Edad"
                    autocomplete="off"
                  />
                </div>
              </div>

              <div class="space-y-3">
                <h3 class="text-base sm:text-lg font-semibold text-gray-800 mb-4">Pruebas de Drogas</h3>

                <!-- Componente para cada prueba de droga -->
                <div
                  v-for="test in drugTests"
                  :key="test.key"
                  class="p-3 sm:p-4 bg-gray-50 rounded-lg mb-3"
                >
                  <div class="mb-3">
                    <span class="font-medium text-gray-700 text-sm block">{{ test.label }}</span>
                  </div>
                  <div class="flex space-x-2">
                    <button
                      type="button"
                      @click="updateDrugTest(test.key, 'NEGATIVA')"
                      :class="[
                        'flex-1 px-3 py-3 rounded-lg text-sm font-medium transition-colors touch-manipulation',
                        formData[test.key] === 'NEGATIVA'
                          ? 'bg-green-500 text-white'
                          : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                      ]"
                    >
                      NEGATIVA
                    </button>
                    <button
                      type="button"
                      @click="updateDrugTest(test.key, 'POSITIVA')"
                      :class="[
                        'flex-1 px-3 py-3 rounded-lg text-sm font-medium transition-colors touch-manipulation',
                        formData[test.key] === 'POSITIVA'
                          ? 'bg-red-500 text-white'
                          : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                      ]"
                    >
                      POSITIVA
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Botón flotante en la parte inferior -->
      <div class="fixed bottom-0 left-0 right-0 bg-white border-t p-3 sm:p-4 safe-area-inset-bottom">
        <div class="max-w-md mx-auto sm:max-w-2xl">
          <button
            type="button"
            @click="handleGenerateReport"
            class="w-full bg-blue-600 text-white p-3 rounded-lg font-semibold hover:bg-blue-700 transition-colors touch-manipulation"
          >
            Generar Reporte
          </button>
        </div>
      </div>
    </div>

    <!-- Reporte -->
    <div v-else-if="currentView === 'report'" class="min-h-screen bg-white">
      <!-- Header para móvil con controles - OCULTO EN IMPRESIÓN -->
      <div class="sticky top-0 bg-white shadow-sm z-10 p-3 sm:p-4 print:hidden">
        <div class="flex items-center justify-between">
          <button
            type="button"
            @click="backToForm"
            class="p-2 text-gray-600 hover:bg-gray-100 rounded-full touch-manipulation"
          >
            <v-icon>mdi-arrow-left</v-icon>
          </button>
          <button
            type="button"
            @click="handlePrint"
            class="flex items-center space-x-2 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 text-sm touch-manipulation"
          >
            <v-icon color="white" size="small">mdi-download</v-icon>
            <span>Imprimir</span>
          </button>
        </div>
      </div>

      <!-- Contenido del reporte - ESCALA REDUCIDA PARA UNA SOLA PÁGINA -->
      <div class="print:scale-90 print:origin-top-left print:p-2">
        <div class="max-w-4xl mx-auto bg-white p-4 print:max-w-none print:mx-0 print:p-1">
          <div class="border border-gray-300 shadow-lg p-4 print:p-2 print:shadow-none" style="font-family: Arial, sans-serif;">

            <!-- Header compacto -->
            <div class="border-b-2 border-blue-600 pb-2 mb-2">
              <div class="flex items-center justify-between">
                <div class="flex items-center">
                  <div class="w-8 h-8 bg-gradient-to-br from-blue-500 to-blue-600 rounded-full flex items-center justify-center mr-2">
                    <div class="text-white font-bold text-xs">
                      SALUD
                    </div>
                  </div>
                  <div>
                    <h1 class="text-sm font-bold text-gray-800">"SALUD AL ALCANCE DE TODOS"</h1>
                    <p class="text-xs text-gray-600">Laboratorio Médico Especializado</p>
                  </div>
                </div>
                <div class="text-right text-xs text-gray-500">
                  <p>Folio: #{{ randomFolio }}</p>
                  <p>Fecha: {{ currentDate }}</p>
                </div>
              </div>
            </div>

            <!-- Información del paciente compacta -->
            <div class="bg-gray-50 rounded-lg p-2 mb-2">
              <h2 class="text-xs font-bold text-gray-800 mb-1 border-b border-gray-300 pb-1">INFORMACIÓN DEL PACIENTE</h2>
              <div class="grid grid-cols-2 gap-2 text-xs">
                <div>
                  <span class="font-medium text-gray-600">Paciente:</span>
                  <p class="text-gray-800 font-medium">{{ formData.nombre || 'No especificado' }}</p>
                </div>
                <div>
                  <span class="font-medium text-gray-600">Fecha:</span>
                  <p class="text-gray-800">{{ currentDate || 'No especificada' }}</p>
                </div>
                <div>
                  <span class="font-medium text-gray-600">Edad:</span>
                  <p class="text-gray-800">{{ formData.edad || 'No especificada' }}</p>
                </div>
                <div>
                  <span class="font-medium text-gray-600">Solicitado por:</span>
                  <p class="text-gray-800">A QUIEN CORRESPONDA</p>
                </div>
              </div>
            </div>

            <!-- Título del examen -->
            <div class="text-center mb-2">
              <div class="bg-blue-600 text-white py-1 px-3 rounded-lg inline-block">
                <h2 class="text-xs font-bold">PERFIL DE DROGAS DE ABUSO 6</h2>
              </div>
            </div>

            <!-- Tabla de resultados compacta -->
            <div class="mb-2">
              <div class="overflow-hidden rounded-lg border border-gray-200">
                <table class="w-full text-xs">
                  <thead class="bg-gray-100">
                    <tr>
                      <th class="px-2 py-1 text-left font-bold text-gray-700 border-r border-gray-200">PRUEBA</th>
                      <th class="px-2 py-1 text-center font-bold text-gray-700 border-r border-gray-200">RESULTADO</th>
                      <th class="px-2 py-1 text-left font-bold text-gray-700">VALORES REF.</th>
                    </tr>
                  </thead>
                  <tbody class="bg-white">
                    <tr v-for="test in drugTests" :key="test.key">
                      <td class="px-2 py-1 text-gray-700 border-r border-gray-200">{{ test.label }}</td>
                      <td class="px-2 py-1 text-center border-r border-gray-200">
                        <span
                          :class="[
                            'font-bold px-1 py-0.5 rounded-full text-xs',
                            (formData[test.key] || 'NEGATIVA') === 'POSITIVA'
                              ? 'bg-red-100 text-red-700'
                              : 'bg-green-100 text-green-700'
                          ]"
                        >
                          {{ formData[test.key] || 'NEGATIVA' }}
                        </span>
                      </td>
                      <td class="px-2 py-1 text-gray-700">
                        <div class="text-xs">
                          <p>NEG: &lt;150 ng/ml</p>
                          <p>POS: ≥150 ng/ml</p>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <!-- Información técnica compacta -->
            <div class="bg-blue-50 rounded-lg p-2 mb-2">
              <div class="flex items-center justify-between text-xs">
                <div>
                  <span class="font-bold text-gray-700">TÉCNICA:</span>
                  <span class="ml-1 text-gray-600">RIA</span>
                </div>
                <div>
                  <span class="font-bold text-gray-700">MÉTODO:</span>
                  <span class="ml-1 text-gray-600">Inmunocromatografía</span>
                </div>
              </div>
            </div>

            <!-- Fin del informe -->
            <div class="text-center mb-2">
              <div class="bg-gray-800 text-white py-1 px-3 rounded-lg inline-block">
                <p class="text-xs font-bold">FIN DEL INFORME</p>
              </div>
            </div>

            <!-- ESPACIO AMPLIO PARA FIRMA -->
            <div class="my-8 print:my-6">
              <div class="h-16 print:h-12"></div>
            </div>

            <!-- Información del profesional compacta -->
            <div class="border-t-2 border-gray-200 pt-2">
              <div class="text-center space-y-1">
                <p class="text-xs font-bold text-gray-800">ATENTAMENTE</p>
                <p class="text-xs font-bold text-gray-800">Q.F.B ELIUTH GARCIA CRUZ</p>
                <p class="text-xs text-gray-600">CED.PROF. 4362774</p>
                <p class="text-xs text-gray-600">MEDICINA GENERAL: FLEBOLOGIA, AUDIOLOGIA</p>
                <p class="text-xs text-gray-600">Av República del Salvador S/N Colonia centro Atotonilco de Tula</p>

                <div class="mt-1 pt-1 border-t border-gray-200">
                  <p class="text-xs font-medium text-gray-700">Asistente Médico</p>
                  <p class="text-xs text-gray-600">Linn Castillo - 7731333631</p>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
app.component("antidoping-page", {
    template: "#antidoping-page",
    data: () => ({
        currentView: 'form',
        errors: [],
        randomFolio: Math.floor(Math.random() * 10000),

        formData: {
            nombre: '',
            fecha: new Date().toISOString().split('T')[0],
            edad: '',
            canabinoides: 'NEGATIVA',
            cocaina: 'NEGATIVA',
            anfetaminas: 'NEGATIVA',
            metanfetaminas: 'NEGATIVA',
            morfina: 'NEGATIVA',
            benzodiazepinas: 'NEGATIVA'
        },

        drugTests: [
            { key: 'canabinoides', label: 'CANABINOIDES' },
            { key: 'cocaina', label: 'COCAINA' },
            { key: 'anfetaminas', label: 'ANFETAMINAS' },
            { key: 'metanfetaminas', label: 'METANFETAMINAS' },
            { key: 'morfina', label: 'MORFINA OPIACEOS' },
            { key: 'benzodiazepinas', label: 'BENZODIAZEPINAS' }
        ]
    }),

    computed: {
        currentDate() {
            return new Date().toLocaleDateString()
        }
    },

    methods: {
        clearErrors() {
            if (this.errors.length > 0) {
                this.errors = []
            }
        },

        updateDrugTest(key, value) {
            this.formData[key] = value
            this.clearErrors()
        },

        validateForm() {
            const validationErrors = []

            if (!this.formData.nombre || this.formData.nombre.trim() === '') {
                validationErrors.push('Nombre del paciente')
            }

            if (!this.formData.fecha) {
                validationErrors.push('Fecha')
            }

            if (!this.formData.edad || this.formData.edad.toString().trim() === '') {
                validationErrors.push('Edad')
            }

            return validationErrors
        },

        handleGenerateReport() {
            const validationErrors = this.validateForm()

            if (validationErrors.length > 0) {
                this.errors = validationErrors
                return
            }

            this.errors = []
            this.currentView = 'report'
        },

        backToForm() {
            this.currentView = 'form'
        },

        handlePrint() {
            window.print()
        }
    },

    mounted() {
        // Agregar estilos CSS para impresión
        const style = document.createElement('style')
        style.textContent = `
            @media print {
                @page {
                    size: A4;
                    margin: 0.5in;
                }

                body {
                    margin: 0;
                    padding: 0;
                    font-size: 10px;
                }

                .print\\:hidden {
                    display: none !important;
                }

                .print\\:scale-90 {
                    transform: scale(0.9) !important;
                }

                .print\\:origin-top-left {
                    transform-origin: top left !important;
                }

                .print\\:p-1 {
                    padding: 0.25rem !important;
                }

                .print\\:p-2 {
                    padding: 0.5rem !important;
                }

                .print\\:max-w-none {
                    max-width: none !important;
                }

                .print\\:mx-0 {
                    margin-left: 0 !important;
                    margin-right: 0 !important;
                }

                .print\\:shadow-none {
                    box-shadow: none !important;
                }

                .print\\:my-6 {
                    margin-top: 1.5rem !important;
                    margin-bottom: 1.5rem !important;
                }

                .print\\:h-12 {
                    height: 3rem !important;
                }
            }

            input {
                color: #1f2937 !important;
                background-color: #ffffff !important;
            }

            input::placeholder {
                color: #9ca3af !important;
            }

            * {
                print-color-adjust: exact !important;
                -webkit-print-color-adjust: exact !important;
            }
        `
        document.head.appendChild(style)
    }
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

.safe-area-inset-bottom {
    padding-bottom: constant(safe-area-inset-bottom);
    padding-bottom: env(safe-area-inset-bottom);
}

.touch-manipulation {
    touch-action: manipulation;
}

.space-y-4 > :not([hidden]) ~ :not([hidden]) {
    --tw-space-y-reverse: 0;
    margin-top: calc(1rem * calc(1 - var(--tw-space-y-reverse)));
    margin-bottom: calc(1rem * var(--tw-space-y-reverse));
}

.space-y-3 > :not([hidden]) ~ :not([hidden]) {
    --tw-space-y-reverse: 0;
    margin-top: calc(0.75rem * calc(1 - var(--tw-space-y-reverse)));
    margin-bottom: calc(0.75rem * var(--tw-space-y-reverse));
}

.space-y-1 > :not([hidden]) ~ :not([hidden]) {
    --tw-space-y-reverse: 0;
    margin-top: calc(0.25rem * calc(1 - var(--tw-space-y-reverse)));
    margin-bottom: calc(0.25rem * var(--tw-space-y-reverse));
}

.space-x-2 > :not([hidden]) ~ :not([hidden]) {
    --tw-space-x-reverse: 0;
    margin-right: calc(0.5rem * var(--tw-space-x-reverse));
    margin-left: calc(0.5rem * calc(1 - var(--tw-space-x-reverse)));
}

@media (min-width: 640px) {
    .sm\\:grid {
        display: grid;
    }

    .sm\\:grid-cols-2 {
        grid-template-columns: repeat(2, minmax(0, 1fr));
    }

    .sm\\:gap-4 {
        gap: 1rem;
    }

    .sm\\:space-y-0 > :not([hidden]) ~ :not([hidden]) {
        --tw-space-y-reverse: 0;
        margin-top: calc(0px * calc(1 - var(--tw-space-y-reverse)));
        margin-bottom: calc(0px * var(--tw-space-y-reverse));
    }

    .sm\\:max-w-2xl {
        max-width: 42rem;
    }

    .sm\\:p-4 {
        padding: 1rem;
    }

    .sm\\:p-6 {
        padding: 1.5rem;
    }

    .sm\\:text-2xl {
        font-size: 1.5rem;
        line-height: 2rem;
    }

    .sm\\:text-lg {
        font-size: 1.125rem;
        line-height: 1.75rem;
    }
}
</style>