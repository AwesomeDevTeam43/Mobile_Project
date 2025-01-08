
# Social Shop
Projeto Desenvolvido no ambito da Disicplina Desenvolvimento de Jogos para Plataformas Móveis do curso Engenharia e Desenvolvimento de Jogos Digitais.

#

Feito por :

Gonçalo Oliveira Araújo - 27928

João Manuel Freitas Ribeiro – 27926

Tiago Miguel Dias Miranda - 27937

     


# Sobre o Projeto

A Social Shop é uma rede social onde o utilizador poderá procurar por produtos, ver o seu preço, ler mais sobre o produto, ver as avaliações e deixar a sua própria avaliação.

![Screenshot 2025-01-06 153434](https://github.com/user-attachments/assets/a55f6572-d953-4d9a-a5b5-5159a229db5f)

# 

Ao inicializar a APP o utilizador será solicitado a realizar o login, caso não possua uma conta deverá proceder ao registo. Todo o sistema de Login e Registro está hospedado na Firebase.

![image](https://github.com/user-attachments/assets/bf778dc6-f989-498a-b962-d9655a05967c)


#

Após efetuar o Login o utilizador será redirecionado para a página Home onde os produtos são exibidos. Os produtos são importados diretamente da Firebase.

![image](https://github.com/user-attachments/assets/8096179c-4b0b-4606-975d-893b5bbbbe31)


#

Na TopBar o utilizador conta com ferramentas para ajudar na procura por um produto, possibilitando a organização da ordem de exibição de produtos por pârametros ou usar a barra de pesquisa para procurar pelo nome de um produto em específico.

![image](https://github.com/user-attachments/assets/86fd353c-0d08-447f-8164-c49783683187)
![image](https://github.com/user-attachments/assets/93e57888-cd3d-4da2-a4d0-2cc6a681a717)

#

Ao clicar em um produto, o utilizador irá acessar uma página com informações mais detalhadas acerca do produto. Terá tambem um botão que adiciona o determinado produto á sua lista de favoritos que pode ser consultada no Menu de Profile e estão disponiveis as reviews deixadas por outro utilizadores e um botão para adicionar a própria review. 

![image](https://github.com/user-attachments/assets/6c142948-786d-4490-8e66-cbee21ac8f99)
![image](https://github.com/user-attachments/assets/5ddaa779-f945-4c61-ad38-735ff8c1b932)



#

Na BottomBar o utilizador pode tambem alternar entre a página Home e a página Profile que contem as suas informações, o botão que dá acesso á lista de produtos favoritos e um botão de Logout. A lista de favoritos esta estruturada de maneira semelhante á página Home, mas apenas mostra os produtos que o utilizador tem marcados como favoritos.

![image](https://github.com/user-attachments/assets/7ac40545-af2c-4a4d-8fea-f4e3a8ed48df)
![image](https://github.com/user-attachments/assets/53f1bf13-8cd2-4147-894e-09241f6f3180)

#

Ao carregar dar back no Android o utilzador irá receber um pedido de confirmação para dar logout.

![image](https://github.com/user-attachments/assets/c819f92a-4b2d-4a86-80a4-a49082044cb4)

#

Todos, os produtos exibido na nossa App, como foi referido anteriormente, estão guardados na Firebase, porém inicialmente foram extraidos de 2 APIs através de programas esternos gerados por IA.

A primeira API que usamos foi retirada do site https://fakeapi.platzi.com/, onde usamos um script em JavaScript que importa todos os produtos e as suas categorias para a nossa base de dados.

```JavaScript
const axios = require('axios');
const { initializeApp } = require('firebase/app');
const { getFirestore, collection, addDoc, getDocs, query, where } = require('firebase/firestore');

// Configuração Firebase
const firebaseConfig = {
    apiKey: "AIzaSyAVRui7juWiuuYxfLnLyTcJ29qFbPcj7rE",
    authDomain: "mobile-project-socialshop.firebaseapp.com",
    projectId: "mobile-project-socialshop",
    storageBucket: "mobile-project-socialshop.firebasestorage.app",
    messagingSenderId: "550755317362",
    appId: "1:550755317362:web:a002914c461027032674eb",
    measurementId: "G-1C372MKPNH"
  };

// Inicializar Firebase
const app = initializeApp(firebaseConfig);
const db = getFirestore(app);

const importData = async () => {
  try {
    // Fetch produtos da API
    const response = await axios.get('https://api.escuelajs.co/api/v1/products');
    const products = response.data;

    // Criar categorias e associar IDs
    const categoryMap = new Map(); // Guardar IDs das categorias para evitar duplicados
    for (const product of products) {
      const categoryName = product.category.name;

      if (!categoryMap.has(categoryName)) {
        // Adicionar categoria ao Firestore
        const categoryDoc = await addDoc(collection(db, "categories"), {
          name: categoryName,
          image: product.category.image,
        });

        // Guardar o ID da categoria
        categoryMap.set(categoryName, categoryDoc.id);
      }
    }

    // Adicionar produtos ao Firestore com referência à categoria
    for (const product of products) {
      const categoryName = product.category.name;
      const categoryId = categoryMap.get(categoryName);

      await addDoc(collection(db, "products"), {
        title: product.title,
        price: product.price,
        description: product.description,
        images: product.images,
        category: categoryId, // Referência ao ID da categoria
      });
    }

    console.log('Categorias e produtos importados com sucesso!');
  } catch (error) {
    console.error('Erro ao importar dados:', error);
  }
};

importData();

```

Como a primeira API tinha poucos produtos e muitos deles eram apenas produtos de teste, sem imagem nem descrição, então procuramos outra API.

A API que escolhemos usar foi a https://fakestoreapi.in/, mas esta estava em um formato diferente, um ficheiro em typescript com todos os dados dos produtos, para importar isso usamos outro codigo gerado por IA, desta vez em Python para ser capaz de ler o ficheiro e selecionar apenas as informações nessessarias e importa-las para a nossa Firebase.


``` Python
import time
import json
import re
import firebase_admin
from firebase_admin import credentials, firestore

# Initialize Firebase Admin
cred = credentials.Certificate("path/to/your/firebase-admin-sdk.json")
firebase_admin.initialize_app(cred)
db = firestore.client()

# Path to the TypeScript file
typescript_file_path = "products.ts"

def extract_products_from_typescript(file_path):
    """
    Reads the TypeScript file, extracts the Products array, and parses it into Python objects.
    """
    with open(file_path, "r", encoding="utf-8") as file:
        content = file.read()

    # Regex to extract the JSON-like array from the TypeScript file
    match = re.search(r"export const Products = (\[.*?\]);", content, re.DOTALL)
    if not match:
        raise ValueError("Could not find 'Products' array in the TypeScript file.")

    products_json = match.group(1)
    return json.loads(products_json)

def add_products_to_firestore(products):
    """
    Adds products to Firestore, creating categories if they don't already exist.
    """
    for product in products:
        try:
            print(f"Processing product: {product['title']}")

            # Check if category exists or create it
            category_query = db.collection("categories").where("name", "==", product["category"]).get()
            if category_query:
                category_id = category_query[0].id
            else:
                category_doc = db.collection("categories").add({"name": product["category"], "image": ""})
                category_id = category_doc[1].id

            # Add product
            db.collection("products").add({
                "title": product["title"],
                "price": product["price"],
                "description": product["description"],
                "images": [product["image"]],
                "category": category_id,
            })
            
            print(f"Successfully added product: {product['title']}")
        except Exception as e:
            print(f"Error adding product {product['title']}: {e}")
        
        # Add delay
        time.sleep(0.2)

# Main logic
if __name__ == "__main__":
    try:
        products = extract_products_from_typescript(typescript_file_path)
        print(f"Found {len(products)} products to process.")
        add_products_to_firestore(products)
    except Exception as e:
        print(f"An error occurred: {e}")
```

