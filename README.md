# Spring AI Project

This project demonstrates how to integrate **Spring Boot with Spring AI** to build intelligent, LLM-powered backend services.  
In addition to basic LLM interaction, the project also implements **Retrieval-Augmented Generation (RAG)** using Spring AI’s vector store support.

---

## Tech Stack

- Java
- Spring Boot
- Spring AI
- Ollama
- Spring AI Vector Store
- JUnit 5
- Maven

---

## AI Models Used

The project uses a locally hosted Ollama model:

- **llama3.2:3b**

Running models locally enables offline development and avoids external API dependencies.

---

## Retrieval-Augmented Generation (RAG)

This project includes a complete **RAG pipeline** built using **Spring AI Vector Store**.

### RAG Implementation Overview

- Uses the **`spring-ai-vector-store`** dependency
- A vector store database is used to persist document embeddings
- Enables the LLM to retrieve relevant context before generating responses

### Dataset Used

- **Top 500 movies** extracted from Kaggle’s *The Movies Dataset*
- Dataset source:  
  https://www.kaggle.com/datasets/rounakbanik/the-movies-dataset

### Data Processing Flow

1. Movie data is extracted from the dataset
2. Content is **split into smaller chunks**
3. Each chunk is converted into embeddings
4. Embeddings are stored in the **Spring AI Vector Store**
5. During queries, relevant chunks are retrieved and injected into the prompt

This approach improves factual accuracy and grounds the LLM responses in real data.

---

## Key Benefits of RAG

- Reduces hallucinations
- Enables domain-specific knowledge
- Scales better than prompt-only approaches
- Demonstrates real-world enterprise AI patterns using Spring AI

---
